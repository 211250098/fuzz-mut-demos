package edu.nju.mutest;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.nju.mutest.http.MyFile;

import java.io.*;
import java.util.*;

/**
 * A demo for executing test suite against mutants
 */
public class DemoMutantExecution {
    private static String res;

    // Use fixed test suite in this demo.
    public static String[][] MutantExecute() throws IOException, InterruptedException {
        StringBuffer sb = new StringBuffer();
        File tsDir = new File("D:/NJU/Code/fuzz-mut-demos/mutest-demo/src/main/java/edu/nju/mutest/exampleTest");
        File mutPoolDir = new File("D:/NJU/Code/fuzz-mut-demos/mutest-demo/pool");
        System.out.println("[LOG] Test suite dir: " + tsDir.getAbsolutePath());
        System.out.println("[LOG] Mutant pool dir: " + mutPoolDir.getAbsolutePath());

        // Locate all tests
        List<File> tsFiles = getAllFiles(tsDir, ".java");
        List<String> tests = new ArrayList<>();
        for (File f : tsFiles) {
            try {
                CompilationUnit cu = StaticJavaParser.parse(f);
                Optional<PackageDeclaration> pd;
                if (isTestSuite(cu) && (pd = cu.getPackageDeclaration()).isPresent()) {
                    String className = f.getName().substring(0, f.getName().lastIndexOf("."));
                    tests.add(pd.get().getName().asString() + '.' + className);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.printf("[LOG] Locate %d test files\n", tests.size());
        sb.append("Locate ").append(tests.size()).append(" test files\n");

        // Locate all mutants
        File[] fns = mutPoolDir.listFiles();
        if (fns == null) {
            System.out.println("[LOG] Find no mutants!" );
            System.exit(0);
        }
        List<File> mutDirs = Arrays.stream(fns)
                .filter(f -> !f.getName().startsWith(".")).toList();
        int mutNum = mutDirs.size();

        System.out.printf("[LOG] Locate %d mutants\n", mutNum);
        sb.append("Locate ").append(mutNum).append(" mutants\n");

        // Execute each mutant
        System.out.println("[LOG] Start to execute mutants...");
        sb.append("Start to execute mutants...\n");

        int killedCnt = 0;
        for (File mutDir : mutDirs) {
            String mutName = mutDir.getName();
            compile(mutDir, mutDir, tsDir);

            System.out.println("[LOG] -------------------------------------------------");
            sb.append("-------------------------------------------------\n");

            System.out.println("[LOG] Execute " + mutName);
            sb.append("Execute ").append(mutName).append("\n");

            boolean killed = false;
            // Execute each Main Method in test suites
            for (String ts : tests) {
                if (execute(mutDir.getAbsolutePath(), ts)) {
                    killed = true;
                    break;
                }
            }
            if (killed) {
                killedCnt++;

                System.out.println("[LOG] Killed " + mutName);
                sb.append("Killed ").append(mutName).append("\n");

            } else {

                System.out.println("[LOG] Survived " + mutName);
                sb.append("Survived ").append(mutName).append("\n");

            }
        }
        // Calculate mutation score
        System.out.println("[LOG] =================================================");
        sb.append("=================================================\n");

        System.out.printf("[LOG] Stats: %d/%d(#killed/#total), score=%.2f%n",
                killedCnt, mutNum, calScore(killedCnt, mutNum));
        sb.append("Stats: ").append(killedCnt).append("/").append(mutNum).append("(#killed/#total), score=").append(calScore(killedCnt, mutNum));
        res = sb.toString();
        String[][] temp = new String[1][];
        temp[0] = res.split("\n");
        return temp;
    }

    public static MyFile[] getMutFiles() throws IOException {
        File mutPool = new File("D:/NJU/Code/fuzz-mut-demos/mutest-demo/pool");
        List<File> files = getAllFiles(mutPool, ".java");
        MyFile[] mutFiles = new MyFile[files.size()];
        for (int i = 0; i < files.size(); i++) {
            File file = files.get(i);
            BufferedReader br = new BufferedReader(new FileReader(file));
            StringBuilder content = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                content.append(line).append("\n");
            }
            br.close();
            MyFile myFile = new MyFile("mut-" + (i + 1), content.toString().split("\n"));
            mutFiles[i] = myFile;
        }
        return mutFiles;
    }

    private static List<File> getAllFiles(File tsDir, String suffix) {
        List<File> AllFiles = new ArrayList<>();
        if (!tsDir.isFile()) {//文件夹
            File[] files = tsDir.listFiles();
            if (tsDir.exists() && files != null) {
                for (File file : files) {
                    AllFiles.addAll(getAllFiles(file, suffix));
                }
            }
        }
        else {//文件
            if (tsDir.getAbsolutePath().endsWith(suffix)) {
                AllFiles = List.of(tsDir);
            }
        }
        return AllFiles;
    }

    private static boolean isTestSuite(CompilationUnit cu) {
        return cu.findAll(MethodDeclaration.class).stream().anyMatch(DemoMutantExecution::isMainMethod);
    }

    private static boolean isMainMethod(MethodDeclaration md) {
        return md.getNameAsString().equals("main")
                && md.getModifiers().stream().anyMatch(m -> m.getKeyword() == Modifier.Keyword.PUBLIC)
                && md.getModifiers().stream().anyMatch(m -> m.getKeyword() == Modifier.Keyword.STATIC)
                && md.getParameters().size() == 1
                && md.getParameters().get(0).getTypeAsString().equals("String[]");
    }

    /**
     * Demo execute once.
     */
    private static boolean execute(String mutPath, String tsPath) throws IOException, InterruptedException {
        // Construct executor
        ProcessBuilder pb = new ProcessBuilder("java", "-cp", mutPath, tsPath);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        // Wait for execution to finish, or we cannot get exit value.
        p.waitFor();

        // Read execution info
        String line;
        while (true) {
            line = br.readLine();
            if (line == null) break;
            System.out.println(line);
        }

        // 0 means survived, not 0 means killed.
        return p.exitValue() != 0;
    }

    private static void execute(String...cmd) throws IOException, InterruptedException {
        // Construct executor
        ProcessBuilder pb = new ProcessBuilder(cmd);
        pb.redirectErrorStream(true);
        Process p = pb.start();

        BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
        // Wait for execution to finish, or we cannot get exit value.
        p.waitFor();

        // Read execution info
        String line;
        while (true) {
            line = br.readLine();
            if (line == null) break;
            System.out.println(line);
        }

        // 0 means survived, not 0 means killed.
        p.exitValue();
    }

    private static double calScore(int killedCnt, int totalNum) {
        return ((double) killedCnt / (double) totalNum) * 100;
    }


    //echo "javac -d $MUT_DIR $SRC_FILE"
    //    javac -d "$MUT_DIR" "$SRC_FILE"
    public static void compile(File mut, File... srcFiles) throws IOException, InterruptedException {
        if (mut == null || !mut.isDirectory() || srcFiles == null || srcFiles.length == 0) {
            throw new IllegalArgumentException("Invalid arguments provided");
        }
        List<String> cmdList = new ArrayList<>(List.of("javac", "-d", mut.getAbsolutePath()));
        for (File srcFile : srcFiles) {
            cmdList.addAll(getAllFiles(srcFile, ".java").stream().map(File::getAbsolutePath).toList()
            );
        }
        execute(cmdList.toArray(new String[0]));
    }

    private String getRes() {
        return res;
    }
}
