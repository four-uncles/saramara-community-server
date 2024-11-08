package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.web.bind.annotation.RestController;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(
        packages = "com.kakao.saramaracommunity.bucket",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ControllerArchitectureTest {

    private static final String CONTROLLER_PACKAGE = "..controller";
    private static final String CONTROLLER_PORT_PACKAGE = "..controller.port";
    private static final String CONTROLLER_NAME = "Controller";
    private static final String SERVICE_INTERFACE_NAME = "Service";

    @ArchTest
    static final ArchRule controller_패키지의_의존성_검사를_검증한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PACKAGE)
                    .should().dependOnClassesThat().resideInAPackage(CONTROLLER_PORT_PACKAGE);

    @ArchTest
    static final ArchRule controller_패키지_내_클래스_이름은_Controller로_끝나야_한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PACKAGE)
                    .should().haveSimpleNameEndingWith(CONTROLLER_NAME);

    @ArchTest
    static final ArchRule controller_port_패키지_내_서비스_인터페이스_이름은_Service로_끝나야_한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PORT_PACKAGE)
                    .should().haveSimpleNameEndingWith(SERVICE_INTERFACE_NAME);

    @ArchTest
    static final ArchRule controller_port_패키지의_서비스는_추상화된_Interface_타입이어야_한다 =
            classes()
                    .that().resideInAnyPackage(CONTROLLER_PORT_PACKAGE)
                    .should().beInterfaces()
                    .andShould().onlyBeAccessed().byClassesThat().resideInAPackage(CONTROLLER_PACKAGE);

    @ArchTest
    static final ArchRule controller_패키지의_클래스는_반드시_RestController_어노테이션이_선언되어야_한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PACKAGE)
                    .should().beAnnotatedWith(RestController.class);

}
