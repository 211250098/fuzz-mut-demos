package edu.nju.mutest;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import edu.nju.mutest.http.FileUploadController;
import edu.nju.mutest.mutator.*;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Demo source-level mutation engine using javaparser.
 */
public class DemoSrcMutationEngine {

    private static final Map<String, Class> mutatorName2Class = Map.of(
            "abs", ABS_Mutator.class,
            "aor", AOR_Mutator.class,
            "lcr", LCR_Mutator.class,
            "ror", ROR_Mutator.class,
            "uoi", UOI_Mutator.class
    );

    public static void main(String[] args) throws IOException {


        // Read in original program(s).

//        File srcFile = FileUploadController.getFile();
        File srcFile = new File("C:/Users/admin/Desktop/fuzz-mut-demos/mut-cases/Sorting/edu/nju/ise/sorting/Sorting.java");
        File outDir = new File("C:/Users/admin/Desktop/fuzz-mut-demos/mutest-demo/pool");
        System.out.println("[LOG] Source file: " + srcFile.getAbsolutePath());
        System.out.println("[LOG] Output dir: " + outDir.getAbsolutePath());

        // Initialize mutator(s).
        CompilationUnit cu = StaticJavaParser.parse(srcFile);
        Mutator mutator = new LCR_Mutator(cu);

        // Locate mutation points.
        mutator.locateMutationPoints();

        // Fire off mutation! Mutants can be wrapped.
        List<CompilationUnit> mutCUs = mutator.mutate();
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
            System.out.println("[LOG] Delete existing outDir." );
        }
        boolean mkdirs = outDir.mkdirs();
        if (mkdirs)
            System.out.println("[LOG] Create outDir: " + outDir.getAbsolutePath());


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

        // Write mutant to local.
        String pattern = "mut-%d/%s";
        for (int i = 0 ; i < mutants.size(); i++) {
            // Create directory to preserve the mutant
            File srcFileDir = new File(outDir, String.format(pattern, i + 1, packPath));
            mkdirs = srcFileDir.mkdirs();
            if (mkdirs)
                System.out.println("[LOG] Create src file dir: " + srcFileDir.getAbsolutePath());
            // Write mutant to local.
            File mutSrcFile = new File(srcFileDir, srcFileName);
            FileWriter fw = new FileWriter(mutSrcFile);
            fw.write(mutants.get(i).toString());
            fw.close();
        }

    }


}
