package com.uniovi.sdi2223entrega1n;

import com.uniovi.sdi2223entrega1n.entities.User;
import com.uniovi.sdi2223entrega1n.pageobjects.*;
import com.uniovi.sdi2223entrega1n.repositories.OffersRepository;
import com.uniovi.sdi2223entrega1n.services.UsersService;
import com.uniovi.sdi2223entrega1n.util.SeleniumUtils;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class Sdi2223Entrega1NApplicationTests {

    static String PathFirefox = "C:\\Program Files\\Mozilla Firefox\\firefox.exe";
    // TODO: Eliminar y dejar una ruta
    //static String Geckodriver  ="A:\\Escritorio\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Path\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Users\\Tomás\\Downloads\\OneDrive_1_7-3-2023\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    //static String Geckodriver = "C:\\Users\\UO253628\\Downloads\\PL-SDI-Sesión5-material\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    static String Geckodriver = "C:\\Users\\kikoc\\Dev\\sellenium\\geckodriver-v0.30.0-win64.exe";
    //static String PathFirefox = "/Applications/Firefox.app/Contents/MacOS/firefox-bin";
    //Ruta Manu (cambiar)
    //static String Geckodriver = "C:\\Users\\Usuario\\Desktop\\SDI\\sesion5\\PL-SDI-Sesión5-material\\geckodriver-v0.30.0-win64.exe";
    static WebDriver driver = getDriver(PathFirefox, Geckodriver);
    static String BASE_ENDPOINT = "http://localhost:8090";

    // Endpoint para mostrar el listado de usuarios (Ver UserController)
    static final String USER_LIST_ENDPOINT = BASE_ENDPOINT + "/user/list";

    // Endpoint para mostrar la vista de conversaciones de un usuario.
    static final String CONVERSATION_LIST_ENDPOINT = BASE_ENDPOINT + "/conversation/list";


    // Enpoint para mostrar la vista de login
    static final String LOGIN_ENDPOINT = BASE_ENDPOINT + "/login";

    @Autowired
    private OffersRepository offersRepository;

    @Autowired
    private UsersService userService;

    public static WebDriver getDriver(String PathFirefox, String Geckodriver) {
        System.setProperty("webdriver.firefox.bin", PathFirefox);
        System.setProperty("webdriver.gecko.driver", Geckodriver);
        driver = new FirefoxDriver();
        return driver;
    }

    @BeforeEach
    public void setUp() {
        driver.navigate().to(BASE_ENDPOINT);
    }

    //Después de cada prueba se borran las cookies del navegador
    @AfterEach
    public void tearDown() {
        driver.manage().deleteAllCookies();
    }

    //Antes de la primera prueba
    @BeforeAll
    static public void begin() {
    }

    //Al finalizar la última prueba
    @AfterAll
    static public void end() {
        // Cerramos el navegador al finalizar las pruebas
        driver.quit();
    }

    //    [Prueba1] Registro de Usuario con datos válidos.
    @Test
    @Order(1)
    void PR01() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo@gmail.com", "Josefo", "Perez", "77777", "77777");
        //Comprobamos que hemos ido a la pagina de home, confirmando que el registro se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    //    [Prueba2] Registro de Usuario con datos inválidos (email vacío, nombre vacío, apellidos vacíos).
    @Test
    @Order(2)
    void PR02() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "", "", "", "77777", "77777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver, PO_Properties.getSPANISH());
    }

    //    [Prueba3] Registro de Usuario con datos inválidos (repetición de contraseña inválida).
    @Test
    @Order(3)
    void PR03() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo@gmail.com", "Josefo", "Perez", "77777", "773777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver, PO_Properties.getSPANISH());
    }

    //    [Prueba4] Registro de Usuario con datos inválidos (email existente).
    @Test
    @Order(4)
    void PR04() {
        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo1@gmail.com", "Josefo", "Perez", "77777", "77777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());

        //Nos movemos al formulario de registro
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
        PO_HomeView.clickOption(driver, "signup", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos INVALIDOS
        PO_SignUpView.fillForm(driver, "JoseFo1@gmail.com", "Josefo", "Perez", "77777", "77777");
        //Comprobamos que seguimos en la pantalla de registro
        PO_SignUpView.checkSignUpPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba5] Inicio de sesión con datos válidos (administrador).
    @Test
    @Order(5)
    void PR05() {
        //Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario administrador
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que hemos ido a la pagina de home, confirmando que el inicio de sesión se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba6] Inicio de sesión con datos válidos (usuario estándar).
    @Test
    @Order(6)
    void PR06() {
        //Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario1@email.com", "123456");
        //Comprobamos que hemos ido a la pagina de home, confirmando que el inicio de sesión se ha completado con exito
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba7] Inicio de sesión con datos inválidos (usuario estándar, campo email y contraseña vacíos)
    @Test
    @Order(7)
    void PR07() {
        //Nos movemos al formulario de inicio de sesión
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos INVALIDOS
        PO_LoginView.fillForm(driver, "", "");
        //Comprobamos que seguimos en la pantalla de inicio de sesión
        PO_LoginView.checkLoginPage(driver, PO_Properties.getSPANISH());
    }

    //[Prueba11] Mostrar el listado de usuarios y comprobar que se muestran todos los que existen en el
    //sistema.
    @Test
    @Order(11)
    void PR011() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");


        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //Sacamos la lista de usaurios del sistema
        List<User> usersSystem = userService.getUsers();
        Assertions.assertEquals(usersSystem.size(), usersSystem.size());
        //Comprobamos uno a uno
        PO_UserListView.compareOneByOneTwoUsersLists(driver, usersList, usersSystem);
    }

    //[Prueba12] Ir a la lista de usuarios, borrar el primer usuario de la lista, comprobar que la lista se actualiza
    //y dicho usuario desaparece.
    @Test
    @Order(12)
    void PR012() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");

        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //guardamos tamaño para comporbar
        int s1 = usersList.size();
        //Primer usuario y marcaje de su checkbox
        WebElement firstUser = usersList.get(0);

        PO_UserListView.markCheckBoxUser(driver, firstUser);
        //Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);

        //Actualizamos la lista
        usersList = PO_UserListView.getUsersList(driver);
        //Guardamos segundo tamaño y vemos q no es el mismo, comprobamos que decrementó en 1
        int s2 = usersList.size();
        Assertions.assertNotEquals(s1, s2);
        Assertions.assertEquals(s1, s2 + 1);
    }

    //[Prueba13] Ir a la lista de usuarios, borrar el último usuario de la lista, comprobar que la lista se actualiza
//y dicho usuario desaparece.
    @Test
    @Order(13)
    void PR013() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");

        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //guardamos tamaño para comporbar
        int s1 = usersList.size();
        //Ultimo usuario y marcaje de su checkbox
        WebElement lastUser = usersList.get(usersList.size() - 1);
        PO_UserListView.markCheckBoxUser(driver, lastUser);
        //Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);
        //Actualizamos la lista
        usersList = PO_UserListView.getUsersList(driver);
        //Guardamos segundo tamaño y vemos q no es el mismo, comprobamos que decrementó en 1
        int s2 = usersList.size();
        Assertions.assertNotEquals(s1, s2);
        Assertions.assertEquals(s1, s2 + 1);
    }

    //[Prueba14] Ir a la lista de usuarios, borrar 3 usuarios, comprobar que la lista se actualiza y dichos
    //usuarios desaparecen.
    @Test
    @Order(14)
    void PR014() {
        //Nos movemos al formulario de inicio de sesion
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "admin@email.com", "admin");
        //Comprobamos que seguimos en la pantalla de registro
        PO_HomeView.checkWelcomeToPage(driver, PO_Properties.getSPANISH());
        //Accedemos a la lista de users
        driver.get("http://localhost:8090/user/list");

        //Sacamos la lista de usuarios q hay
        List<WebElement> usersList = PO_UserListView.getUsersList(driver);
        //guardamos tamaño para comporbar
        int s1 = usersList.size();
        //Sacamos los tres primeros usuarios y marcamos de sus checkboxes
        WebElement u1 = usersList.get(0);
        WebElement u2 = usersList.get(1);
        WebElement u3 = usersList.get(2);
        PO_UserListView.markCheckBoxUser(driver, u1);
        PO_UserListView.markCheckBoxUser(driver, u2);
        PO_UserListView.markCheckBoxUser(driver, u3);
        //Borramos dandole al boton
        PO_UserListView.clickDeleteButton(driver);
        //Actualizamos la lista
        usersList = PO_UserListView.getUsersList(driver);
        //Guardamos segundo tamaño y vemos q no es el mismo, comprobamos que decrementó en 1
        int s2 = usersList.size();
        Assertions.assertNotEquals(s1, s2);
        Assertions.assertEquals(s1, s2 + 3);
    }

    // [Prueba 15]. Añadir nueva oferta con datos válidos.
    @Test
    @Order(15)
    public void PR015() {
        // Crear nuevo usuario
        SeleniumUtils.registerNewUser(driver, "miemail444@email.com", "123456");

        String newOfferText = "Coche marca Renault 1";

        // Acceder a la vista de añadir una nueva oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOfferMenu");

        // Rellenar campos del formulario con valores válidos.
        PO_OfferView.fillForm(driver, newOfferText, "Coche de los años 90", 2000.50);

        // Comprobar que se muestra en el listado de ofertas
        List<WebElement> offers = PO_View.checkElementBy(driver, "text", newOfferText);
        Assertions.assertEquals(1, offers.size());
    }

    // [Prueba 16]. Añadir una nueva oferta con datos inválidos -> precio negativo.
    @Test
    @Order(16)
    public void PR016() {
        // Iniciar sesion
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", "miemail444@email.com");

        // Acceder a la vista de añadir una nueva oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOfferMenu");

        // Rellenar campos del formulario con valores inválidos.
        PO_OfferView.fillForm(driver, "Coche marca Renault", "Coche de los años 90", -1.0);

        // Comprobar que se muestra el error en el formulario.
        PO_OfferView.checkErrorMessage(driver, PO_Properties.getSPANISH(), "error.offer.price.range");
    }

    // [Prueba 17]. Listado de ofertas propias de un usuario. Comprobar que se muestran todas las ofertas
    // publicadas por dicho usuario.
    @Test
    @Order(17)
    public void PR017() {
        // Iniciar sesion
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", "miemail444@email.com");

        // Acceder a la vista de listado de ofertas
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "listOfferMenu");

        // Comprobar número elementos de tabla con número de elementos BBDD
        int offerCountFromUserOnDatabase = offersRepository.findAllBySeller("miemail444@email.com").size();

        // Obtener número de filas de la tabla de la vista del listado de ofertas
        int rowCount = SeleniumUtils.countTableRows(driver, "//table[@class='table table-hover']/tbody/tr");

        // Verificar que el número de registros mostrados es correcto
        Assertions.assertEquals(offerCountFromUserOnDatabase, rowCount);
    }

    // [Prueba18]. Baja de una oferta - Borrar primera oferta de la lista.
    @Test
    @Order(18)
    public void PR018() {
        // Registrar nuevo usuario
        SeleniumUtils.registerNewUser(driver, "miemail666@email.com", "123456");

        String sampleText1 = "Coche de segunda mano";
        String sampleText2 = "Vestido de comunión";
        String sampleText3 = "Baraja de cartas";

        // Iniciar sesion, añadir una nueva oferta
        PO_OfferView.addSampleOffer(driver, sampleText1);
        PO_OfferView.addSampleOffer(driver, sampleText2);
        PO_OfferView.addSampleOffer(driver, sampleText3);

        // Click en el enlace eliminar del primer elemento de la lista
        PO_OfferView.clickDeleteButton(driver, 0);

        // Comprobar que el primer elemento no se muestra en la tabla
        PO_OfferView.checkOfferNotExistsOnPage(driver, sampleText1);

        // Comprobar que el número de oferta es 3
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(2, offerList.size());
    }

    // [Prueba19]. Baja de una oferta - Borrar última oferta de la lista.
    @Test
    @Order(19)
    public void PR019() {
        // Registrar nuevo usuario
        SeleniumUtils.registerNewUser(driver, "miemail777@email.com", "123456");

        String sampleTextLast = "Coche se segunda mano";
        String sampleText2 = "Vestido de comunión";
        String sampleText3 = "Baraja de cartas";

        // Iniciar sesion, añadir una nueva oferta
        PO_OfferView.addSampleOffer(driver, sampleText2);
        PO_OfferView.addSampleOffer(driver, sampleText3);
        PO_OfferView.addSampleOffer(driver, sampleTextLast);

        // Click en el enlace eliminar del ultimo elemento de la lista
        PO_OfferView.clickDeleteButton(driver, 2);

        // Comprobar que el ultimo elemento no se muestra en la tabla
        PO_OfferView.checkOfferNotExistsOnPage(driver, sampleTextLast);

        // Comprobar que el número de ofertas, tras eliminar la última oferta, es 2
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());
        Assertions.assertEquals(2, offerList.size());

    }

    /**
     * [Prueba 20] Hacer una búsqueda con el campo vacío y comprobar que se muestra la página que
     * corresponde con el listado de las ofertas existentes en el sistema
     */
    @Test
    @Order(20)
    public void PR020() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario7@email.com", "123456");
        String invalid = "";
        PO_AllOfferView.SearchInvalid(driver, invalid);

        //Comprobar que el numero es 0
        List<WebElement> offerList = SeleniumUtils.waitLoadElementsBy(driver, "free", "//tbody/tr",
                PO_View.getTimeout());

        Assertions.assertEquals(5, offerList.size());

        //Cierro sesion
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    /**
     * [Prueba21] Hacer una búsqueda escribiendo en el campo un texto que no exista y comprobar que se
     * muestra la página que corresponde, con la lista de ofertas vacía.
     */
    @Test
    @Order(21)
    public void PR021() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario7@email.com", "123456");
        String invalid = "asdasd";
        PO_AllOfferView.SearchInvalid(driver, invalid);

        //Comprobar que el numero es 0
        SeleniumUtils.textIsNotPresentOnPage(driver, "//tbody/tr");

        //Cierro sesion
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    /**
     * [Prueba22] Sobre una búsqueda determinada (a elección del desarrollador),
     * comprar una oferta que deja un saldo positivo en el contador del comprador.
     * Comprobar que el contador se actualiza correctamente en la vista del comprador.
     */
    @Test
    @Order(22)
    public void PR022() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario7@email.com", "123456");
        //Entramos a la vista de comprar y compramos la oferta 62 que su precio es valido
        String buttonName = "buyOffer62";
        PO_AllOfferView.buyOffer(driver,buttonName);
        //Sacamos el valor del wallet
        String value = PO_AllOfferView.seeWallet(driver);
        //Lo comparamos con el precio restado
        Assertions.assertEquals(value,"54.0");
        //Cierro sesion
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }

    /**
     * [Prueba23] Sobre una búsqueda determinada (a elección del desarrollador),
     * comprar una oferta que deja un saldo 0 en el contador del comprador.
     * Comprobar que el contador se actualiza correctamente en la vista del comprador.
     */
    @Test
    @Order(23)
    public void PR023() {
        // Registrar nuevo usuario
        SeleniumUtils.registerNewUser(driver, "usuario8@email.com", "123456");
        // Accedemos al menu de añadir una oferta
        PO_NavView.selectDropdownById(driver, "gestionOfertasMenu", "gestionOfertasDropdown", "addOfferMenu");
        // Añadimos una oferta nueva
        PO_OfferView.fillForm(driver, "Prueba 23", "Descripción prueba 23", 54.0);
        //Cierro sesion
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");

        //Accedo con el comprador
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario7@email.com", "123456");
        //Entramos a la vista de comprar y compramos la oferta 107 que su precio es igual al wallet
        String buttonName = "buyOffer107";
        PO_AllOfferView.buyOffer(driver,buttonName);
        //Sacamos el valor del wallet
        String value = PO_AllOfferView.seeWallet(driver);
        //Lo comparamos con el precio restado
        Assertions.assertEquals(value,"0.0");

    }

    /**
     * [Prueba24] Sobre una búsqueda determinada (a elección del desarrollador),
     * intentar comprar una oferta que esté por encima de saldo disponible del comprador.
     * Y comprobar que se muestra el mensaje de saldo no suficiente.
     */
    @Test
    @Order(24)
    public void PR024() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Rellenamos con datos validos del usuario estandar
        PO_LoginView.fillForm(driver, "usuario7@email.com", "123456");
        //Entramos a la vista de comprar y compramos la oferta 45 que su precio es invalido
        String buttonName = "buyOffer45";
        PO_AllOfferView.buyOffer(driver,buttonName);
        //Buscamos que aparezca en la pagina la label
        boolean isDisplayed = driver.findElement(By.id("errorPrecio")).isDisplayed();
        Assertions.assertEquals(true,isDisplayed);
        //Cierro sesion
        PO_HomeView.clickOption(driver, "logout", "class", "btn btn-primary");
    }
    //[Prueba26] Sobre una búsqueda determinada de ofertas (a elección de desarrollador), enviar un mensaje
//a una oferta concreta. Se abriría dicha conversación por primera vez. Comprobar que el mensaje aparece
//en la conversación.
    @Test
    @Order(26)
    public void PR026() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "usuario6@email.com", "123456");



        //vamos a la vista que queremos, directamente haciendo la búsqueda que queremos, en nuestro caso Carro
        driver.get("http://localhost:8090/offer/allList?searchText=Carro");

        //Click the link
        PO_ConversationsView.clickConversationsLink(driver);

        //we count the number of rows
        WebElement table = driver.findElement(By.id("tableMessages"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsInit = rows.size();

        // Write a message
        PO_ConversationsView.writeMessage(driver, "MensajePrueba");
        // Send the message
        PO_ConversationsView.sendMessage(driver);

        // Check if the message was sent by counting again the number of rows
        table = driver.findElement(By.id("tableMessages"));
        List<WebElement> rows2 = table.findElements(By.tagName("tr"));
        int numRowsAfter = rows2.size();

        Assertions.assertEquals(numRowsInit, numRowsAfter-1);
    }

    //[Prueba27] Enviar un mensaje a una conversación ya existente accediendo desde el botón/enlace
//“Conversación”. Comprobar que el mensaje aparece en la conversación
    @Test
    @Order(27)
    public void PR027() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "usuario1@email.com", "123456");
        //vamos a la vista que queremos, directamente haciendo la búsqueda que queremos, en nuestro caso Carro
        driver.get("http://localhost:8090/offer/allList?searchText=Carro");

        //Click the link
        PO_ConversationsView.clickConversationsLink(driver);

        //we count the number of rows
        WebElement table = driver.findElement(By.id("tableMessages"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsInit = rows.size();

        // Write a message
        PO_ConversationsView.writeMessage(driver, "MensajePrueba");
        // Send the message
        PO_ConversationsView.sendMessage(driver);

        // Check if the message was sent by counting again the number of rows
        table = driver.findElement(By.id("tableMessages"));
        List<WebElement> rows2 = table.findElements(By.tagName("tr"));
        int numRowsAfter = rows2.size();

        Assertions.assertEquals(numRowsInit, numRowsAfter-1);
    }


    //[Prueba28] Mostrar el listado de conversaciones ya abiertas. Comprobar que el listado contiene la
    //cantidad correcta de conversaciones.
    @Test
    @Order(28)
    public void PR028() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "usuario4@email.com", "123456");
        //vamos a la vista que queremos, directamente haciendo la búsqueda que queremos, en nuestro caso Carro
        driver.get("http://localhost:8090/conversation/mylist");
        WebElement table = driver.findElement(By.id("tableMyOffer"));
        List<WebElement> rows2 = table.findElements(By.tagName("tr"));
        int numRows = rows2.size();

        Assertions.assertEquals(numRows, 2);

    }
    // [Prueba 30]. Acceso sin autenticación a la opción de listado de usuarios.
    @Test
    @Order(30)
    public void PR30() {
        // Acceso por url sin estar autenticado.
        driver.navigate().to(USER_LIST_ENDPOINT);

        // Comprobar que se redirecciona a la vista de login
        Assertions.assertEquals(LOGIN_ENDPOINT, driver.getCurrentUrl());
    }

    // [Prueba 31]. Acceso sin autenticación al listado de conversaciones de un
    // usuario estandar. Se debe redireccionar al formulario de login.
    @Test
    @Order(31)
    public void PR31() {
        // Acceso por url sin estar autenticado.
        driver.navigate().to(CONVERSATION_LIST_ENDPOINT);

        // Comprobar que se redirecciona a la vista de login
        Assertions.assertEquals(LOGIN_ENDPOINT, driver.getCurrentUrl());
    }

    // [Prueba 32]. Usuario estandar autenticado accede a una opción disponible solo
    // para usuarios administradores.
    @Test
    @Order(32)
    public void PR32() {

        String email = "miemail888@email.com";

        // Registrar nuevo usuario
        SeleniumUtils.registerNewUser(driver, email, "123456");


        // Acceder a una opción para usuarios administradores
        driver.navigate().to(BASE_ENDPOINT + PO_AdminView.ADMIN_DASHBOARD_ENDPOINT);

        // Comprobar que se redirecciona a la vista de login
        Assertions.assertEquals(LOGIN_ENDPOINT, driver.getCurrentUrl());
    }

    // [Prueba 33]. Usuario administrador autenticado visualiza los logs generados
    // en una serie de iteraciones. Generar al menos dos interacciones de cada
    // tipo y comprobar que el listado incluye los correspondientes logs.
    @Test
    @Order(33)
    public void PR33() {
        // Acción previa: Borrar todos los logs
        PO_AdminView.deleteAllLogsWithLogout(driver);

        // -- TIPO LOGIN-EX
        // Iniciar sesion como admin - LOG-EX
        SeleniumUtils.signInIntoAccount(driver, "ADMIN", "admin@email.com");

        // Cerrar la sesión del usuario admin
        // TIPO_LOGOUT
        PO_NavView.clickLogout(driver);
        String userEmail1 = "user0000234@email.com";

        // Registrar nuevo usuario
        // -- TIPO ALTA
        SeleniumUtils.registerNewUser(driver, userEmail1, "123456");

        // -- TIPO LOGOUT
        // Cerrar la sesión del usuario 1
        PO_NavView.clickLogout(driver);

        // Iniciar sesión con el usuario creado e introducir la contraseña mal - LOGIN-ERR
        // -- TIPO LOGIN-ERR
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", userEmail1, "123");

        // -- TIPO LOGIN-EX
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", userEmail1, "123456");

        // Rellenar campos del formulario con valores válidos.
        // -- TIPO PET
        PO_OfferView.addSampleOfferWithDescriptionAndPrice(driver, "Oferta de prueba 1.1", "Coche de los años 90", 2000.50);

        // -- TIPO LOGOUT
        // Cerrar la sesión del usuario 1
        PO_NavView.clickLogout(driver);

        String userEmail2 = "user0000444@email.com";

        // Registrar nuevo usuario
        // -- TIPO ALTA
        SeleniumUtils.registerNewUser(driver, userEmail2, "123456");

        // -- TIPO LOGOUT
        PO_NavView.clickLogout(driver);
        // Iniciar sesión con el usuario creado e introducir la contraseña mal
        // -- TIPO LOGIN-ERR
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", userEmail2, "123");

        // -- TIPO LOGIN-EX
        SeleniumUtils.signInIntoAccount(driver, "STANDARD", userEmail2, "123456");

        // -- TIPO PET
        // Acceder a la vista de añadir una nueva oferta
        PO_OfferView.addSampleOfferWithDescriptionAndPrice(driver, "Oferta de prueba 1.2", "Piso céntrico en Oviedo centro", 2000.50);

        // Rellenar campos del formulario con valores válidos.
        // -- TIPO PET

        // -- TIPO LOGOUT
        // Cerrar la sesión del usuario 2
        PO_NavView.clickLogout(driver);

        // Total de logs esperado: +1 por la peticion de borrado de logs previamente
        // realizada
        int expectedLogs = 27;

        // Ir a la vista de logs
        // -- TIPO PET
        PO_AdminView.goToListOfLogsView(driver);

        // Obtener el número de logs de la tabla
        int rowCount = SeleniumUtils.countTableRows(driver, "//table[@class='table table-striped px-3 my-3']/tbody/tr");

        // Comprobar que el número de registros mostrados es correcto
        Assertions.assertEquals(expectedLogs, rowCount);
    }

    // [Prueba 34]. Autenticado como usuario administrador, ir a visualización de
    // logs, pulsar en el botón de borrar logs y comprobar que se eliminan los logs
    // de la base de datos.
    @Test
    @Order(34)
    public void PR34() {
        // Iniciar sesion como administrador y eliminar todos los logs
        PO_AdminView.deleteAllLogsWithLogout(driver);

        // Total de logs esperado: 1
        int expectedLogs = 1;

        SeleniumUtils.signInIntoAccount(driver, "ADMIN", PO_AdminView.ADMIN_EMAIL);

        // Ir a la vista de logs
        PO_NavView.clickOption(driver, PO_AdminView.ADMIN_DASHBOARD_ENDPOINT, "id", "viewLogsMenuItem");

        // Hacer click en el boton de eliminar logs
        //PO_NavView.clickOption(driver, PO_AdminView.ADMIN_DELETE_ALL_LOGS_ENDPOINT, "id", "deleteAllLogsButton");
        PO_AdminView.deleteAllLogsWithoutLogout(driver);

        // Obtener el número de logs de la tabla
        int rowCount = SeleniumUtils.countTableRows(driver, "//table[@class='table table-striped px-3 my-3']/tbody/tr");

        // Comprobar que el número de registros mostrados es correcto
        Assertions.assertEquals(expectedLogs, rowCount);
    }

    //[Prueba35] Sobre el listado de conversaciones ya abiertas. Pinchar el enlace Eliminar de la primera y
    //comprobar que el listado se actualiza correctamente
    @Test
    @Order(35)
    public void PR035() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "usuario1@email.com", "123456");
        //vamos a la vista que queremos, directamente haciendo la búsqueda que queremos, en nuestro caso Carro
        driver.get("http://localhost:8090/conversation/list");

        WebElement table = driver.findElement(By.id("tableOtherOffers"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsOriginal = rows.size();

        PO_ConversationsView.clickEliminarOtherOffersFirst(driver);
        WebElement table2 = driver.findElement(By.id("tableOtherOffers"));
        List<WebElement> rows2 = table2.findElements(By.tagName("tr"));
        int numRowsFinal = rows2.size();
        Assertions.assertEquals(numRowsOriginal, numRowsFinal+1);

    }
    //[Prueba36] Sobre el listado de conversaciones ya abiertas, pulsar el enlace Eliminar de la última y
    //comprobar que el listado se actualiza correctamente
    @Test
    @Order(36)
    public void PR036() {
        PO_HomeView.clickOption(driver, "login", "class", "btn btn-primary");
        //Cumplimentamos el registro con datos VALIDOS
        PO_LoginView.fillForm(driver, "usuario1@email.com", "123456");
        //vamos a la vista que queremos, directamente haciendo la búsqueda que queremos, en nuestro caso Carro
        driver.get("http://localhost:8090/conversation/list");

        WebElement table = driver.findElement(By.id("tableOtherOffers"));
        List<WebElement> rows = table.findElements(By.tagName("tr"));
        int numRowsOriginal = rows.size();

        PO_ConversationsView.clickEliminarOtherOffersLast(driver);
        WebElement table2 = driver.findElement(By.id("tableOtherOffers"));
        List<WebElement> rows2 = table2.findElements(By.tagName("tr"));
        int numRowsFinal = rows2.size();
        Assertions.assertEquals(numRowsOriginal, numRowsFinal+1);
    }
}
