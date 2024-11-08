package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.library.Architectures;
import com.tngtech.archunit.library.Architectures.LayeredArchitecture;
import org.springframework.beans.factory.annotation.Autowired;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.Architectures.layeredArchitecture;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.*;

@AnalyzeClasses(
        packages = "com.kakao.saramaracommunity.bucket",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class GlobalArchitectureTest {

    private static final String CONTROLLER_LAYER = "Controller";
    private static final String SERVICE_LAYER = "Service";
    private static final String INFRA_LAYER = "Infrastructure";
    private static final String CONTROLLER_PACKAGE = "..controller";
    private static final String SERVICE_PACKAGE = "..service";
    private static final String INFRA_PACKAGE = "..infrastructure";
    private static final String GLOBAL_PACKAGE = "com.kakao.saramaracommunity.(*)..";

    @ArchTest
    static final LayeredArchitecture 레이어검사_모든_계층의_의존흐름은_순방향_이어야한다 =
            layeredArchitecture()
                    .consideringAllDependencies()
                    .layer(CONTROLLER_LAYER).definedBy(CONTROLLER_PACKAGE)
                    .layer(SERVICE_LAYER).definedBy(SERVICE_PACKAGE)
                    .layer(INFRA_LAYER).definedBy(INFRA_PACKAGE)
                    .whereLayer(CONTROLLER_LAYER).mayNotBeAccessedByAnyLayer()
                    .whereLayer(SERVICE_LAYER).mayOnlyBeAccessedByLayers(CONTROLLER_LAYER)
                    .whereLayer(INFRA_LAYER).mayOnlyBeAccessedByLayers(SERVICE_LAYER);

    @ArchTest
    static final ArchRule 필드주입검사_모든_클래스는_필드주입을_사용하지_않는다 =
            fields()
                    .that().areDeclaredInClassesThat().resideInAPackage(GLOBAL_PACKAGE)
                    .should().notBeAnnotatedWith(Autowired.class);

    @ArchTest
    static final ArchRule 순환참조검사_모든_클래스는_순환_의존성을_가지면_안_된다 =
            slices()
                    .matching(GLOBAL_PACKAGE)
                    .should().beFreeOfCycles();

}
