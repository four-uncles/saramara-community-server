package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.library.Architectures;

import static com.tngtech.archunit.library.Architectures.layeredArchitecture;

@AnalyzeClasses(
        packages = "com.kakao.saramaracommunity.bucket",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class LayerdArchitectureTest {

    @ArchTest
    static final Architectures.LayeredArchitecture layerTest = layeredArchitecture()
            .consideringAllDependencies()
            .layer("Controller").definedBy("..controller")
            .layer("Service").definedBy("..service")
            .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
            .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller");
}
