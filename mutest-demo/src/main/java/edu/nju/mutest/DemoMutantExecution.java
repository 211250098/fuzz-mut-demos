package edu.nju.mutest;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import edu.nju.mutest.http.FileUploadController;

/**
 * A demo for executing test suite against mutants
 */
public class DemoMutantExecution {


    // Use fixed test suite in this demo.
    public static void main(String[] args) throws IOException, InterruptedException {

        File tem = FileUploadController.getFile();
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
                if (IsTestSuite(cu) && (pd = cu.getPackageDeclaration()).isPresent()) {
                    String className = f.getName().substring(0, f.getName().lastIndexOf("."));
                    tests.add(pd.get().getName().asString() + '.' + className);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.printf("[LOG] Locate %d test files%n", tests.size());

        // Locate all mutants
        File[] fns = mutPoolDir.listFiles();
        if (fns == null) {
            System.out.println("[LOG] Find no mutants!" );
            System.exit(0);
        }
        List<File> mutDirs = Arrays.stream(fns)
                .filter(f -> !f.getName().startsWith("."))
                .collect(Collectors.toList());
        int mutNum = mutDirs.size();
        System.out.printf("[LOG] Locate %d mutants%n", mutNum);

        // Execute each mutant
        System.out.println("[LOG] Start to execute mutants...");
        int killedCnt = 0;
        for (File mutDir : mutDirs) {
            String mutName = mutDir.getName();
            System.out.println("[LOG] -------------------------------------------------");
            compile(mutDir, mutDir, tsDir);
            System.out.println("[LOG] Execute " + mutName);
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
            } else {
                System.out.println("[LOG] Survived " + mutName);
            }
        }
        // Calculate mutation score
        System.out.println("[LOG] ======================================================");
        System.out.printf("[LOG] Stats: %d/%d(#killed/#total), score=%.2f%n",
                killedCnt, mutNum, calScore(killedCnt, mutNum));
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

    private static boolean IsTestSuite(CompilationUnit cu) {
        return cu.findAll(MethodDeclaration.class)
                .stream()
                .anyMatch(md -> md.getNameAsString().equals("main")
                        && md.getModifiers().stream().anyMatch(m -> m.getKeyword() == Modifier.Keyword.PUBLIC)
                        && md.getModifiers().stream().anyMatch(m -> m.getKeyword() == Modifier.Keyword.STATIC)
                        && md.getParameters().size() == 1
                        && md.getParameters().get(0).getTypeAsString().equals("String[]"));
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

    private static int execute(String...cmd) throws IOException, InterruptedException {
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
        return p.exitValue();
    }

    private static double calScore(int killedCnt, int totalNum) {
        return ((double) killedCnt / (double) totalNum) * 100;
    }

    public static boolean compile(File dst, File... srcFiles) throws IOException, InterruptedException {
        List<String> cmdList = new ArrayList<>(List.of("javac", "-d", dst.getAbsolutePath()));
        for (File srcFile : srcFiles) {
            cmdList.addAll(
                    getAllFiles(srcFile, ".java").stream()
                            .map(File::getAbsolutePath)
                            .collect(Collectors.toList())
            );
        }
        return execute(cmdList.toArray(new String[0])) == 0;
    }
}
