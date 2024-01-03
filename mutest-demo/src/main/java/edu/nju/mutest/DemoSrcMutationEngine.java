package edu.nju.mutest;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ParserConfiguration;
import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.symbolsolver.JavaSymbolSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.CombinedTypeSolver;
import com.github.javaparser.symbolsolver.resolution.typesolvers.ReflectionTypeSolver;
import edu.nju.mutest.mutator.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Source-level mutation engine using javaParser.
 */
public class DemoSrcMutationEngine {
    public static void main(String[] args) throws IOException {

        File src_file = new File("C:\\Users\\admin\\Desktop\\fuzz-mut-demos\\mutest-demo\\original_test");
        File outDir = new File("C:\\Users\\admin\\Desktop\\fuzz-mut-demos\\mutest-demo/pool");
        List<File> files = List.of(Objects.requireNonNull(src_file.listFiles()));
        File srcFile = files.get(0);
        System.out.println("[LOG] Source file: " + srcFile.getAbsolutePath());
        System.out.println("[LOG] Output dir: " + outDir.getAbsolutePath());

        String MutatorName = "UOI";//改成上传的变异算子的名字
        Mutator mutator = null;
        // Initialize mutator(s).

        //在ABS跟UOI中需要这个配置
        ParserConfiguration parserConfiguration = new ParserConfiguration().setSymbolResolver(new JavaSymbolSolver(new CombinedTypeSolver(new ReflectionTypeSolver())));
        JavaParser javaParser = new JavaParser(parserConfiguration);



        CompilationUnit cu = javaParser.parse(srcFile).getResult().get();
        List<CompilationUnit> mutCUs = new ArrayList<>(List.of(cu));

        switch (MutatorName) {
            case "ABS" -> {
                mutator = new ABS_Mutator(cu);
            }
            case "AOR" -> {
                mutator = new AOR_Mutator(cu);
            }
            case "LCR" -> {
                mutator = new LCR_Mutator(cu);
            }
            case "ROR" -> {
                mutator = new ROR_Mutator(cu);
            }
            case "UOI" -> {
                mutator = new UOI_Mutator(cu);
            }
        }

        System.out.println("[LOG] use mutator: " + MutatorName);

        mutCUs.addAll(mutator.mutate());

        mutCUs.remove(cu);
        System.out.printf("[LOG] Generate %d mutants.\n", mutCUs.size());

        // Preserve to local.
        preserveToLocal(outDir, srcFile, cu, mutCUs);
    }

    /**
     * Write mutants to disk.
     */
    private static void preserveToLocal(
            File outDir,
            File srcFile,
            CompilationUnit cu,
            List<CompilationUnit> mutants) throws IOException {

        // Recreate outDir if it is existed.
        if (outDir.exists()) {
            FileUtils.forceDelete(outDir);
            System.out.println("[LOG] Delete existing outDir.");
        }
        boolean mkdirs = outDir.mkdirs();
        if (mkdirs) {
            System.out.println("[LOG] Create outDir: " + outDir.getAbsolutePath());
        }

        // Path relevant to package info
        String packPath = "";

        // Get file name.
        String srcFileName = srcFile.getName();

        // Get package info.
        Optional<PackageDeclaration> opPD = cu.getPackageDeclaration();
        if (opPD.isPresent()) {
            // Turn package info like 'edu.nju.ise' to path 'edu/nju/ise/'
            String packInfo = opPD.get().getName().asString();
            packPath = packInfo.replace('.', '/');
        }

        String pattern = "mut-%d/%s";
        for (int i = 0; i < mutants.size(); i++) {
            // Create directory to preserve the mutant
            File srcFileDir = new File(outDir, String.format(pattern, i + 1, packPath));
            mkdirs = srcFileDir.mkdirs();
            if (mkdirs) {
                System.out.println("[LOG] Create src file dir: " + srcFileDir.getAbsolutePath());
            }
            // Write mutant to local.
            File mutSrcFile = new File(srcFileDir, srcFileName);
            FileWriter fw = new FileWriter(mutSrcFile);
            fw.write(mutants.get(i).toString());
            fw.close();
        }
    }

}
