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
    private static final String SERVICE_PACKAGE = "..service";
    private static final String CONTROLLER_NAME = "Controller";
    private static final String SERVICE_INTERFACE_NAME = "Service";

    @ArchTest
    static final ArchRule 패키지종속성검사_Controller는_controller_port_패키지의_서비스_인터페이스를_의존해야_한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PACKAGE)
                    .should().dependOnClassesThat().resideInAPackage(CONTROLLER_PORT_PACKAGE);

    @ArchTest
    static final ArchRule 패키지종속성검사_Controller는_service_패키지의_구현_클래스를_직접_의존하면_안_된다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PACKAGE)
                    .should().onlyDependOnClassesThat()
                    .resideOutsideOfPackage(SERVICE_PACKAGE);

    @ArchTest
    static final ArchRule 패키지구조검사_controller_패키지_내_클래스_이름은_Controller로_명명되어야_한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PACKAGE)
                    .should().haveSimpleNameEndingWith(CONTROLLER_NAME);

    @ArchTest
    static final ArchRule 패키지구조검사_controller_port_패키지의_서비스_인터페이스는_Service로_명명되어야_하며_Interface_타입이어야한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PORT_PACKAGE)
                    .should().haveSimpleNameEndingWith(SERVICE_INTERFACE_NAME)
                    .andShould().beInterfaces()
                    .andShould().onlyBeAccessed().byClassesThat().resideInAPackage(CONTROLLER_PACKAGE);

    @ArchTest
    static final ArchRule 어노테이션검사_controller_패키지의_클래스는_반드시_RestController_어노테이션이_선언되어야_한다 =
            classes()
                    .that().resideInAPackage(CONTROLLER_PACKAGE)
                    .should().beAnnotatedWith(RestController.class);

}
