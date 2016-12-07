package test;

public class testDB {

    /*
    ApplicationContext context;
    ApplicationContext context2;

    IGrupaServera grupaServera;
    IServisi servisi;
    IServerServisi serverServisi;
    IServer server;
    IDBMaster IDB;

    @Before
    public void setUp() {
        context = new AnnotationConfigApplicationContext(DBServiceConfig.class);
        context2 = new AnnotationConfigApplicationContext(MasterConfig.class);

        grupaServera = context.getBean(IGrupaServera.class);
        servisi = context.getBean(IServisi.class);
        serverServisi = context.getBean(IServerServisi.class);
        server = context.getBean(IServer.class);

        IDB = context2.getBean(IDBMaster.class);
    }

    @Test
    public void grupaservisa_test() {
//        System.err.println(grupaServera.getAll());
//        System.err.println(servisi.getAll());
//        System.err.println("----------------");
//        System.err.println(serverServisi.getAll());
//        System.err.println("----------------");
//        System.err.println("----------------");
//        System.err.println();

//        Servisi se = servisi.getByID(1);
//        System.err.println("***ID=1*** " + se);
//
//        se.setNaziv(se.getNaziv() + "***");
//        try {
//            servisi.update(se);
//        } catch (Exception ex) {
//            Logger.getLogger(testDB.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        System.err.println("\n\n---> " + se);
//
//        Server s = server.getByID(3);
//        System.err.println("***-> " + s);
//
//        System.err.println("---------------------\n\n");
//        System.err.println(server.getChildren(s));
//        System.err.println("---------------------\n\n");
//
//        System.err.println();
//        System.err.println("--------------------------------------------------------");
//        System.err.println("--------------------------------------------------------");
//        System.err.println();
//
//        Servisi se = servisi.getByID(3);
//        System.err.println("***-> " + se);
//
//        System.err.println("---------------------\n\n");
//        System.err.println(servisi.getChildren(se));
//        System.err.println("---------------------\n\n");
//        
//        
//        System.err.println();
//        System.err.println("--------------------------------------------------------");
//        System.err.println("--------------------------------------------------------");
//        System.err.println("--------------------------------------------------------");
//        System.err.println();
//
//        Grupaservera gs = grupaServera.getByID(1);
//        System.err.println("***-> " + gs);
//
//        System.err.println("---------------------\n\n");
//        System.err.println(grupaServera.getChildren(gs));
//        System.err.println("---------------------\n\n");
    }

    @Test
    public void test2() {
        IGrupaServera igs = IDB.IGS();
        Grupaservera gs = igs.getByID(1);

        System.err.println("***test 2 ***");
        System.err.println("GS, ID=1 : " + gs);
        System.err.println("--------------");
        System.err.println("children: " + igs.getChildren(gs));

        System.err.println("--------------------------------------------------");
        System.err.println("--------------------------------------------------");
        System.err.println("--------------------------------------------------");

        IServer is = IDB.IS();
        Server s = is.getByID(1);

        System.err.println("***test 2 ***");
        System.err.println("S, ID=1 : " + s);
        System.err.println("--------------");
        System.err.println("children: " + is.getChildren(s));
    }
*/
}
