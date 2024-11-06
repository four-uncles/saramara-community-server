package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

@AnalyzeClasses(
        packages = "com.kakao.saramaracommunity.bucket",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ControllerArchitectureTest2 {

    @ArchTest
    static final ArchRule controller_패키지_내_클래스_이름은_Controller로_끝나야_한다 = ArchRuleDefinition.classes()
            .that().resideInAPackage("..controller..")
            .and().resideOutsideOfPackage("..controller.port..")
            .should().haveSimpleNameEndingWith("Controller")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controller_port_패키지_내_서비스_인터페이스_이름은_Service로_끝나야_한다 = ArchRuleDefinition.classes()
            .that().resideInAPackage("..controller.port..")
            .should().haveSimpleNameEndingWith("Service")
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controller_패키지의_의존성_검사를_검증한다 = ArchRuleDefinition.classes()
            .that().resideInAPackage("..controller..")           // controller 패키지에 있는 클래스들에 대해
            .should().onlyDependOnClassesThat()
            .resideInAnyPackage(
                    "..controller.port..",
                    "..controller..",
                    "..bucket.dto..",
                    "..common..",
                    "..util..",
                    "io.swagger.v3..",
                    "java..",
                    "org.springframework.."
            )
            .allowEmptyShould(true);

    @ArchTest
    static final ArchRule controller_패키지의_클래스는_반드시_RestController_어노테이션이_선언되어야_한다 = ArchRuleDefinition.classes()
            .that().resideInAPackage("..bucket.controller..")
            .and().resideOutsideOfPackage("..controller.port..")
            .should().beAnnotatedWith(RestController.class);

}
