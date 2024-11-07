package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.web.bind.annotation.RestController;

import static com.kakao.saramaracommunity.architecture.support.CommonPackageProvider.combineWithCommonPackages;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(
        packages = "com.kakao.saramaracommunity.bucket",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ControllerArchitectureTest2 {

    private static final String PACKAGE_DEPENDENCIES_RULE = "[Controller] controller 패키지의 Controller 클래스는 controller.port 패키지의 서비스 인터페이스를 의존해야 합니다.";
    private static final String CONTROLLER_NAME_RULE = "[Controller] controller 패키지의 Controller 클래스는 Controller로 명명되어야 합니다.";
    private static final String SERVICE_NAME_RULE = "[Controller] controller.port 패키지의 서비스 인터페이스는 Service로 명명되어야 합니다.";
    private static final String SERVICE_TYPE_RULE = "[Controller] controller.port 패키지의 서비스는 Interface 타입이어야 합니다.";
    private static final String ANNOTATION_RULE = "[Controller] Controller 클래스는 @RestController 어노테이션이 선언되어야 합니다.";

    @ArchTest
    static final ArchRule controller_패키지의_의존성_검사를_검증한다 =
            classes()
                .that().resideInAPackage("..controller..")
                .should().dependOnClassesThat()
                .resideInAnyPackage(combineWithCommonPackages("..controller.port.."))
                .because(PACKAGE_DEPENDENCIES_RULE);

    @ArchTest
    static final ArchRule controller_패키지_내_클래스_이름은_Controller로_끝나야_한다 =
            classes()
                .that().resideInAPackage("..controller..")
                .and().resideOutsideOfPackage("..controller.port..")
                .should().haveSimpleNameEndingWith("Controller")
                .because(CONTROLLER_NAME_RULE);

    @ArchTest
    static final ArchRule controller_port_패키지_내_서비스_인터페이스_이름은_Service로_끝나야_한다 =
            classes()
                .that().resideInAPackage("..controller.port..")
                .should().haveSimpleNameEndingWith("Service")
                .because(SERVICE_NAME_RULE);

    @ArchTest
    static final ArchRule controller_port_패키지의_서비스는_추상화된_Interface_타입이어야_한다 =
            classes()
                .that().resideInAnyPackage("..controller.port..")
                .should().beInterfaces()
                .because(SERVICE_TYPE_RULE);

    @ArchTest
    static final ArchRule controller_패키지의_클래스는_반드시_RestController_어노테이션이_선언되어야_한다 =
            classes()
                .that().resideInAPackage("..bucket.controller..")
                .and().resideOutsideOfPackage("..controller.port..")
                .should().beAnnotatedWith(RestController.class)
                .because(ANNOTATION_RULE);

}
