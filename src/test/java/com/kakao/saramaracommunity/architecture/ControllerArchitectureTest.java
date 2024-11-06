package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RestController;

public class ControllerArchitectureTest {

    private JavaClasses javaClasses;

    @BeforeEach
    public void beforeEacsh() {
        javaClasses = new ClassFileImporter()
                .withImportOption(ImportOption.Predefined.DO_NOT_INCLUDE_TESTS) // 테스트 패키지는 제외
                .importPackages("com.kakao.saramaracommunity.bucket..");
    }

    @Test
    @DisplayName("controller 패키지 내 클래스 이름은 Controller로 끝나야 한다.")
    void controllersShouldBeNamedCorrectly() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..controller..")
                .and().resideOutsideOfPackage("..controller.port..")
                .should().haveSimpleNameEndingWith("Controller")
                .allowEmptyShould(true);  
        rule.check(javaClasses);
    }

    @Test
    @DisplayName("controller.port 패키지 내 서비스 인터페이스 이름은 Service로 끝나야 한다.")
    void controllerInPortsShouldBeNamedCorrectly() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..controller.port..")
                .should().haveSimpleNameEndingWith("Service")
                .allowEmptyShould(true);  
        rule.check(javaClasses);
    }

    @Test
    @DisplayName("controller 패키지의 의존성 검사를 검증한다.")
    void controllerDependenciesShouldBeValidated() {
        ArchRule rule = ArchRuleDefinition.classes()
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
        rule.check(javaClasses);
    }

    @Test
    @DisplayName("bucket.controller 패키지의 클래스는 반드시 @RestController 어노테이션이 선언되어야 한다.")
    void controllersShouldBeAnnotatedWithRestController() {
        ArchRule rule = ArchRuleDefinition.classes()
                .that().resideInAPackage("..bucket.controller..")
                .and().resideOutsideOfPackage("..controller.port..")
                .should().beAnnotatedWith(RestController.class);
        rule.check(javaClasses);
    }


}
