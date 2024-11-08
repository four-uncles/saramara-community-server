package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.*;

@AnalyzeClasses(
        packages = "com.kakao.saramaracommunity.bucket",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class GlobalArchitectureTest {

    @ArchTest
    static final Architectures.LayeredArchitecture 모든_계층의_의존흐름은_순방향_이어야한다 =
            layeredArchitecture()
                    .consideringAllDependencies()
                    .layer("Controller").definedBy("..controller")
                    .layer("Service").definedBy("..service")
                    .layer("Infrastructure").definedBy("..infrastructure..")
                    .whereLayer("Controller").mayNotBeAccessedByAnyLayer()
                    .whereLayer("Service").mayOnlyBeAccessedByLayers("Controller")
                    .whereLayer("Infrastructure").mayOnlyBeAccessedByLayers("Service");

    @ArchTest
    static final ArchRule 모든_클래스는_필드주입을_사용하지_않는다 =
            fields()
                    .that().areDeclaredInClassesThat().resideInAPackage("..")
                    .should().notBeAnnotatedWith(Autowired.class);

    @ArchTest
    static final ArchRule 모든_클래스는_순환_의존성을_가지면_안_된다 =
            slices()
                    .matching("com.kakao.saramaracommunity.(*)..")
                    .should().beFreeOfCycles();

}
