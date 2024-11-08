package com.kakao.saramaracommunity.architecture;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import org.springframework.stereotype.Service;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;

@AnalyzeClasses(
        packages = "com.kakao.saramaracommunity.bucket",
        importOptions = ImportOption.DoNotIncludeTests.class
)
public class ServiceArchitectureTest {

    private static final String SERVICE_PACKAGE = "..service";
    private static final String SERVICE_PORT_PACKAGE = "..service.port";
    private static final String CONTROLLER_PORT_PACKAGE = "..controller.port";
    private static final String SERVICE_IMPLEMENT_NAME = "ServiceImpl";

    @ArchTest
    static final ArchRule 패키지종속성검사_Service는_service_port_패키지의_infrastructure_인터페이스를_의존해야_한다 =
            classes()
                    .that().resideInAPackage(SERVICE_PACKAGE)
                    .should().dependOnClassesThat().resideInAPackage(SERVICE_PORT_PACKAGE);

    @ArchTest
    static final ArchRule 패키지구조검사1_service_패키지_내_구현_클래스_이름은_ServiceImpl로_명명되어야_한다 =
            classes()
                    .that().resideInAPackage(SERVICE_PACKAGE)
                    .should().haveSimpleNameEndingWith(SERVICE_IMPLEMENT_NAME);


    @ArchTest
    static final ArchRule 패키지구조검사2_service_port_패키지의_인프라스트럭처_인터페이스는_Interface_타입이어야_한다 =
            classes()
                    .that().resideInAPackage(SERVICE_PORT_PACKAGE)
                    .should().beInterfaces();

    @ArchTest
    static final ArchRule 구현검사_Service_클래스는_controller_port_패키지의_Service_인터페이스를_구현해야_한다 =
            classes()
                    .that().resideInAPackage(SERVICE_PACKAGE)
                    .and().areNotInterfaces().should().implement(resideInAPackage(CONTROLLER_PORT_PACKAGE));

    @ArchTest
    static final ArchRule 어노테이션검사_service_패키지의_구현_클래스는_반드시_Service_어노테이션이_선언되어야_한다 =
            classes()
                    .that().resideInAPackage(SERVICE_PACKAGE)
                    .should().beAnnotatedWith(Service.class);

}
