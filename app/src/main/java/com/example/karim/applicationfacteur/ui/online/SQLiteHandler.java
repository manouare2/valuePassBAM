package com.example.karim.applicationfacteur.ui.online;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;
import java.util.HashMap;

public class SQLiteHandler extends SQLiteOpenHelper {

    private static final String TAG = SQLiteHandler.class.getSimpleName();

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "off_line";


    // Login table name
    private static final String TABLE_USER = "data";
    // table insert
    private static final String TABLE_USER1= "data1";
    private static final String TABLE_USER2= "data2";
    private static final String TABLE_USER3= "data3";
    private static final String TABLE_motif= "motift";
    private static final String Table_mesure= "mesuret";
    private static final String Table_envoi_pere= "envoi_pere";
    private static final String TABLE_division= "division";
    private static final String TABLE_relais ="relais";
    private static final String TABLE_pere ="pere";
    private static final String TABLE_FDR ="FDR";
    private static final String TABLE_client ="client";
    private static final String TABLE_ENVOIS_CL ="envois";
    private static final String TABLE_motif_CL ="motif_cl";
    private static final String TABLE_CL_OFFLINE ="offline_cl";
    private static final String TABLE_objet_CL ="objet_cl";
    private static final String TABLE_objet_DNL ="objet_dnl";

    /**************TABLE MAPS******/
    private static final String TABLE_MAPS ="maps";


    // table motif

    private static final String KEY_IDD = "id";
    private static final String KEY_motif = "motif";
    private static final String KEY_designation = "designation";


    // table motif cl

    private static final String KEY_ID_cl = "id";
    private static final String KEY_motif_cl = "motif";
    private static final String KEY_designation_cl = "designation";

    // table envoi_pere

    private static final String KEY_ID4 = "id";
    private static final String KEY_code_envoii = "code_envoi";
    private static final String KEY_client1 = "client";
    private static final String KEY_telephone1 = "telephone";
    private static final String KEY_adresse1 = "adresse";
    private static final String KEY_crbt1 = "crbt";
    private static final String KEY_login1 = "login";
    private static final String KEY_stat_envoii1 = "envoi";
    private static final String KEY_stat_envoi11 = "envoi1";
    private static final String KEY_service1 = "service";
    private static final String KEY_pod1 = "pod";
    private static final String KEY_designation11= "designation";
    private static final String KEY_mode_liv1 = "mode_liv";
    private static final String KEY_rue1= "rue";
    private static final String KEY_code_postal1= "code_postal";
    private static final String KEY_flag1= "flag";
    private static final String KEY_date1= "date";
    private static final String KEY_adr_relais1= "adr_relais";
    private static final String KEY_relais1= "relais";
    private static final String KEY_agc1= "agc";
    private static final String KEY_adr_agc1= "adr_agc";
    private static final String KEY_obj1= "obj";
    private static final String KEY_tel_exp1= "tel_exp";
    private static final String KEY_code_envoi_pere = "code_envoi_pere";


    // table mesure

    private static final String KEY_IDD1 = "id";
    private static final String KEY_motiff = "motiff";
    private static final String KEY_mesure = "mesure";
    private static final String KEY_statt = "stat";



    // table division

    private static final String KEY_ID_division = "id";
    private static final String KEY_divison = "division";


    // table division

    private static final String KEY_ID_relais = "id";
    private static final String KEY_relais_des = "relais";


    // table data
    private static final String KEY_ID = "id";
    private static final String KEY_commande = "cmd";
    private static final String KEY_client = "client";
    private static final String KEY_telephone = "telephone";
    private static final String KEY_adresse = "adresse";
    private static final String KEY_crbt = "crbt";
    private static final String KEY_login = "login";
    private static final String KEY_stat_envoi= "envoi";
    private static final String KEY_stat_envoi1= "envoi1";
    private static final String KEY_service = "service";
    private static final String KEY_pod= "pod";
    private static final String KEY_designation1= "designation";
    private static final String KEY_mode_liv = "mode_liv";
    private static final String KEY_rue= "rue";
    private static final String KEY_code_postal= "code_postal";
    private static final String KEY_flag= "flag";
    private static final String KEY_date= "date";
    private static final String KEY_adr_relais= "adr_relais";
    private static final String KEY_relais= "relais";
    private static final String KEY_agc= "agc";
    private static final String KEY_adr_agc= "adr_agc";
    private static final String KEY_obj= "obj";
    private static final String KEY_tel_exp= "tel_exp";
    private static final String KEY_envoi_pere= "envoi_pere";

    // table Zfacteur insert commanade


    private static final String KEY_ID3 = "id3";
    private static final String KEY_code_barre = "code_barre";
    private static final String KEY_facteur = "facteur";
    private static final String KEY_txt04 = "txt04";
    private static final String KEY_txt30 = "txt30";
    private static final String KEY_stsma = "stsma";
    private static final String KEY_stat = "stat";
    private static final String KEY_statprec = "statprec";
    private static final String KEY_motif1 = "motif";
    private static final String KEY_mesure1 = "mesure";
    private static final String KEY_modepaiement = "modepaiement";
    private static final String KEY_typepid = "typepid";
    private static final String KEY_pid = "pid";
    private static final String KEY_destinataire = "destinataire";
    private static final String KEY_modeliv = "modeliv";
    private static final String KEY_division = "div";
    private static final String KEY_relaiss = "relais";



    // table user
    private static final String KEY_ID2 = "id2";
    private static final String KEY_user = "user";
    private static final String KEY_pwd = "pwd";


    // table insert

    private static final String KEY_ID1 = "id1";
    private static final String KEY_code_objet = "objet";
    private static final String KEY_code_envoi = "envoi";
    private static final String KEY_agence = "agence";
    private static final String KEY_agent = "agent";
    private static final String KEY_zsd = "zsd";


    //TABLE_pere



    private static final String KEY_ID_pere = "id1";
    private static final String KEY_PERE = "PERE";
    private static final String KEY_PERE_CODE = "PERE_CODE";
    private static final String KEY_SIZE = "SIZE";

    //************************FDR*******************//


    private static final String KEY_ID_collecte= "id";
    private static final String KEY_ID_FDR = "id_fdr";
    private static final String KEY_NUM_POST = "num_poste";
    private static final String KEY_NUM_CLIENT = "num_client";
    private static final String KEY_NOM_CLIENT = "nom_client";
    private static final String KEY_INTERLOCUTEUR = "interlocuteur";
    private static final String KEY_Adresse_client = "adresse_client";
    private static final String KEY_TEL_CLIENT = "tel_client";
    private static final String KEY_DATE_FDR = "date";
    private static final String KEY_HEURE_FDR = "heure";
    private static final String KEY_FACTEUR_FDR = "facteur";
    private static final String KEY_stat_FDR = "stat";
    private static final String KEY_agent_FDR = "agent";
    private static final String KEY_agence_FDR = "agence";
    private static final String KEY_type_cl = "type_cl";
    private static final String KEY_heuretr = "heuretr";
    private static final String KEY_code_2D_CL = "code_2d";
    private static final String KEY_FLAG = "flag";
    private static final String KEY_MOTIF = "motif";
    //HHA 10/06/2020
    private static final String KEY_NB_ENVOI = "nbenvoi";







    //************************client collecte*******************//


    private static final String KEY_ID_TABLE= "id";
    private static final String KEY_ID_client = "id_client";
    private static final String KEY_NOM_CLIENTT = "nom_client";
    private static final String KEY_CONTACT = "contact";
    private static final String KEY_ADRESSE_CLIENT = "adresse_client";
    private static final String KEY_TELEPHONE_CLIENT = "telephone_client";
    private static final String KEY_ID_AGENCE_CLIENT = "id_agence";
    private static final String KEY_AGENCE_CLIENT = "agence_client";
    private static final String KEY_REGION_CLIENT = "region";
    private static final String KEY_CODE_2D= "code2D";



    //************************les envois collecte*******************//


    private static final String KEY_ID_ENVOI= "id";
    private static final String KEY_ID_ENVOI_CL= "id_cl_envoi";
    private static final String KEY_ID_client_ENVOI = "id_client";
    private static final String KEY_code_envoi_CL = "code_envoi";
    private static final String KEY_facteur_CL = "facteur";
    ;


    //************************collecte off line *******************//


    private static final String KEY_ID_collecte_off= "id";
    private static final String KEY_ID_FDR_off = "id_fdr";
    private static final String KEY_NUM_POST_off = "num_poste";
    private static final String KEY_NUM_CLIENT_off = "num_client";
    private static final String KEY_NOM_CLIENT_off = "nom_client";
    private static final String KEY_INTERLOCUTEUR_off = "interlocuteur";
    private static final String KEY_Adresse_client_off = "adresse_client";
    private static final String KEY_TEL_CLIENT_off = "tel_client";
    private static final String KEY_DATE_FDR_off = "date";
    private static final String KEY_HEURE_FDR_off = "heure";
    private static final String KEY_FACTEUR_FDR_off = "facteur";
    private static final String KEY_stat_FDR_off = "stat";
    private static final String KEY_agent_FDR_off = "agent";
    private static final String KEY_agence_FDR_off = "agence";
    private static final String KEY_type_cl_off = "type_cl";
    private static final String KEY_heuretr_off = "heuretr";
    private static final String KEY_code_2D_CL_off = "code_2d";
    private static final String KEY_MOTIF_off = "motif";
    private static final String KEY_code_envoi_off = "code_envoi";
    private static final String KEY_nb_envoi = "nb_envois";





    /***************** num objet collecte ****************/



    private static final String KEY_ID_CL= "id";
    private static final String KEY_FDR_CL = "id_fdr";
    private static final String KEY_user_FDR = "facteur";




    /***************** num objet Distribution ****************/


    private static final String KEY_ID_DNL= "id";
    private static final String KEY_DNL = "id_dnl";
    private static final String KEY_USER_DNL = "facteur";

/******************************MAPS 08/06/2020***************************/
    private static final String KEY_ID_MAPS= "id";
    private static final String KEY_USER_MAPS = "USER";
    private static final String KEY_LATITUDE = "LATITUDE";
    private static final String KEY_LONGITUDE = "LONGITUDE";






    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_LOGIN_TABLE = "CREATE TABLE " + TABLE_USER + " ("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_commande + " TEXT UNIQUE, "
                + KEY_client + " TEXT, "
                + KEY_telephone + " TEXT, "
                + KEY_adresse + " TEXT, "
                + KEY_crbt + " TEXT, "
                + KEY_stat_envoi + " TEXT, "
                + KEY_stat_envoi1 + " TEXT, "
                + KEY_login + " TEXT,"
                + KEY_pod + " TEXT,"
                + KEY_service + " TEXT,"
                + KEY_designation1 + " TEXT,"
                + KEY_mode_liv + " TEXT,"
                + KEY_rue + " TEXT,"
                + KEY_code_postal + " TEXT,"
                + KEY_flag + " TEXT,"
                + KEY_date + " TEXT,"
                + KEY_adr_relais + " TEXT,"
                + KEY_relais + " TEXT,"
                + KEY_agc + " TEXT,"
                + KEY_adr_agc + " TEXT,"
                + KEY_obj + " TEXT,"
                + KEY_tel_exp + " TEXT,"
                + KEY_envoi_pere + " TEXT)";


        db.execSQL(CREATE_LOGIN_TABLE);

        Log.d(TAG, "Database tables created");


        String CREATE_division_TABLE = "CREATE TABLE " + TABLE_division + " ("
                + KEY_ID_division + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_divison + " TEXT)";

        db.execSQL(CREATE_division_TABLE);

        Log.d(TAG, "Database tables created");


        String CREATE_motif_TABLE = "CREATE TABLE " + TABLE_motif + " ("
                + KEY_IDD + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_motif + " TEXT, "
                + KEY_designation + " TEXT)";

        db.execSQL(CREATE_motif_TABLE);

        Log.d(TAG, "Database tables created");


        String CREATE_relais_TABLE = "CREATE TABLE " + TABLE_relais + " ("
                + KEY_ID_relais + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_relais_des + " TEXT)";

        db.execSQL(CREATE_relais_TABLE);

        Log.d(TAG, "Database tables created");


        String CREATE_mesure_TABLE = "CREATE TABLE " + Table_mesure + " ("
                + KEY_IDD1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_motiff + " TEXT, "
                + KEY_mesure + " TEXT, "
                + KEY_statt + " TEXT)";

        db.execSQL(CREATE_mesure_TABLE);

        Log.d(TAG, "Database tables created");


        String CREATE_envoi_pere_TABLE = "CREATE TABLE " + Table_envoi_pere + " ("
                + KEY_ID4 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_code_envoii + " TEXT UNIQUE, "
                + KEY_client + " TEXT, "
                + KEY_telephone + " TEXT, "
                + KEY_adresse + " TEXT, "
                + KEY_crbt + " TEXT, "
                + KEY_stat_envoii1 + " TEXT, "
                + KEY_stat_envoi11 + " TEXT, "
                + KEY_login1 + " TEXT,"
                + KEY_pod1 + " TEXT,"
                + KEY_service1 + " TEXT,"
                + KEY_designation11 + " TEXT,"
                + KEY_mode_liv1 + " TEXT,"
                + KEY_rue1 + " TEXT,"
                + KEY_code_postal1 + " TEXT,"
                + KEY_flag1 + " TEXT,"
                + KEY_date1 + " TEXT,"
                + KEY_adr_relais1 + " TEXT,"
                + KEY_relais1 + " TEXT,"
                + KEY_agc1 + " TEXT,"
                + KEY_adr_agc1 + " TEXT,"
                + KEY_obj1 + " TEXT,"
                + KEY_tel_exp1 + " TEXT,"
                + KEY_code_envoi_pere + " TEXT)";

        db.execSQL(CREATE_envoi_pere_TABLE);

        Log.d(TAG, "Database tables created");


        String CREATE_envoi_pere_TABLE1 = "CREATE TABLE " + TABLE_pere + " ("
                + KEY_ID_pere + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_PERE + " TEXT UNIQUE, "
                + KEY_PERE_CODE + " TEXT, "
                + KEY_SIZE + " TEXT)";

        db.execSQL(CREATE_envoi_pere_TABLE1);

        Log.d(TAG, "Database tables created");


        String CREATE_LOGIN_TABLE1 = "CREATE TABLE " + TABLE_USER1 + "("
                + KEY_ID1 + " INTEGER PRIMARY KEY," + KEY_code_objet + " TEXT," + KEY_code_envoi + " TEXT," + KEY_agence +
                " TEXT," + KEY_agent + " TEXT," + KEY_zsd + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE1);

        Log.d(TAG, "Database tables created");


        String CREATE_LOGIN_TABLE2 = "CREATE TABLE " + TABLE_USER2 + "("
                + KEY_ID2 + " INTEGER PRIMARY KEY," + KEY_user + " TEXT," + KEY_pwd + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE2);

        Log.d(TAG, "Database tables created");


        String CREATE_LOGIN_TABLE3 = "CREATE TABLE " + TABLE_USER3 + "("
                + KEY_ID3 + " INTEGER PRIMARY KEY," + KEY_code_barre + " TEXT," + KEY_facteur + " TEXT," + KEY_txt30 +
                " TEXT," + KEY_txt04 + " TEXT," + KEY_stsma + " TEXT," + KEY_statprec + " TEXT," + KEY_stat + " TEXT," + KEY_motif1 + " TEXT," + KEY_mesure1 + " TEXT," + KEY_modepaiement + " TEXT," + KEY_typepid + " TEXT," + KEY_pid + " TEXT," + KEY_destinataire + " TEXT," + KEY_modeliv + " TEXT," + KEY_division + " TEXT," + KEY_relaiss + " TEXT" + ")";

        db.execSQL(CREATE_LOGIN_TABLE3);

        Log.d(TAG, "Database tables created");


        /********************************creation de la table FDR**********************/


        String CREATE_FDR_TABLE = "CREATE TABLE " + TABLE_FDR + " ("
                + KEY_ID_collecte + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ID_FDR + " TEXT, "
                + KEY_NUM_POST + " TEXT, "
                + KEY_NUM_CLIENT + " TEXT, "
                + KEY_NOM_CLIENT + " TEXT, "
                + KEY_INTERLOCUTEUR + " TEXT, "
                + KEY_TEL_CLIENT + " TEXT, "
                + KEY_Adresse_client + " TEXT, "
                + KEY_DATE_FDR + " TEXT, "
                + KEY_HEURE_FDR + " TEXT, "
                + KEY_FACTEUR_FDR + " TEXT, "
                + KEY_stat_FDR + " TEXT, "
                + KEY_agent_FDR + " TEXT, "
                + KEY_agence_FDR + " TEXT, "
                + KEY_type_cl + " TEXT, "
                + KEY_heuretr + " TEXT, "
                + KEY_code_2D_CL + " TEXT, "
                + KEY_FLAG + " TEXT, "
                + KEY_MOTIF  + " TEXT, "
                + KEY_NB_ENVOI+ " TEXT)";


        db.execSQL(CREATE_FDR_TABLE);

        Log.d(TAG, "Database tables created");


        /********************************creation de la table client collecte*************/


        String CREATE_CLIENT_TABLE = "CREATE TABLE " + TABLE_client + " ("
                + KEY_ID_TABLE + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ID_client + " TEXT UNIQUE, "
                + KEY_NOM_CLIENTT + " TEXT, "
                + KEY_CONTACT + " TEXT, "
                + KEY_ADRESSE_CLIENT + " TEXT, "
                + KEY_TELEPHONE_CLIENT + " TEXT, "
                + KEY_ID_AGENCE_CLIENT + " TEXT, "
                + KEY_AGENCE_CLIENT + " TEXT, "
                + KEY_REGION_CLIENT + " TEXT, "
                + KEY_CODE_2D + " TEXT)";


        db.execSQL(CREATE_CLIENT_TABLE);

        Log.d(TAG, "Database tables created");


        /********************************creation de la table des envois collecte*************/


        String CREATE_ENVOIS_TABLE = "CREATE TABLE " + TABLE_ENVOIS_CL + " ("
                + KEY_ID_ENVOI + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ID_ENVOI_CL + " TEXT, "
                + KEY_ID_client_ENVOI + " TEXT, "
                + KEY_code_envoi_CL + " TEXT, "
                + KEY_facteur_CL + " TEXT)";


        db.execSQL(CREATE_ENVOIS_TABLE);

        Log.d(TAG, "Database tables created");

/************ création des tables des motifs cl**********/

        String CREATE_motif_TABLE_cl = "CREATE TABLE " + TABLE_motif_CL + " ("
                + KEY_ID_cl + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_motif_cl + " TEXT, "
                + KEY_designation_cl + " TEXT)";

        db.execSQL(CREATE_motif_TABLE_cl);

        Log.d(TAG, "Database tables created");


        /********************************creation de la table FDR**********************/


        String CREATE_CL_OFFLINE = "CREATE TABLE " + TABLE_CL_OFFLINE + " ("
                + KEY_ID_collecte_off + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_ID_FDR_off + " TEXT, "
                + KEY_NUM_POST_off + " TEXT, "
                + KEY_NUM_CLIENT_off + " TEXT, "
                + KEY_NOM_CLIENT_off + " TEXT, "
                + KEY_INTERLOCUTEUR_off + " TEXT, "
                + KEY_TEL_CLIENT_off + " TEXT, "
                + KEY_Adresse_client_off + " TEXT, "
                + KEY_DATE_FDR_off + " TEXT, "
                + KEY_HEURE_FDR_off + " TEXT, "
                + KEY_FACTEUR_FDR_off + " TEXT, "
                + KEY_stat_FDR_off + " TEXT, "
                + KEY_agent_FDR_off + " TEXT, "
                + KEY_agence_FDR_off + " TEXT, "
                + KEY_type_cl_off + " TEXT, "
                + KEY_heuretr_off + " TEXT, "
                + KEY_code_2D_CL_off + " TEXT, "
                + KEY_MOTIF_off + " TEXT, "
                + KEY_code_envoi_off  + " TEXT, "
                + KEY_nb_envoi + " TEXT)";


        db.execSQL(CREATE_CL_OFFLINE);



/********************* creation de la table d'objet collecte **********/


        String CREATE_object_TABLE = "CREATE TABLE " + TABLE_objet_CL + " ("
                + KEY_ID_CL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_FDR_CL + " TEXT, "
                + KEY_user_FDR + " TEXT)";


        db.execSQL(CREATE_object_TABLE);






/********************* creation de la table d'objet DNL **********/


        String CREATE_object_TABLE_DNL = "CREATE TABLE " + TABLE_objet_DNL + " ("
                + KEY_ID_DNL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_DNL + " TEXT, "
                + KEY_USER_DNL + " TEXT)";


        db.execSQL(CREATE_object_TABLE_DNL);


/********************************MAPS*****************************/

        String CREATE_MAPS_TABLE = "CREATE TABLE " + TABLE_MAPS + " ("
                +KEY_ID_MAPS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_USER_MAPS + " TEXT, "
                + KEY_LATITUDE + " TEXT, "
                + KEY_LONGITUDE + " TEXT)";

        db.execSQL(CREATE_MAPS_TABLE);



    }
        // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER);

        // Create tables again
        onCreate(db);
    }

   // insert dans Table_USER
    public void addData(String cmd,String client,String telephone,String adresse ,String crbt, String stat_envoi,String stat_envoi1, String login,String pod,String service,String designation,String mode_liv,String rue,String code_postal, String flag,String date,String adr_relais,String relais,String agc,String adr_agc,String obj,String tel_exp,String code_envoi_pere) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_commande, cmd);
        values.put(KEY_client, client);
        values.put(KEY_telephone, telephone);
        values.put(KEY_adresse, adresse);
        values.put(KEY_crbt,crbt);
        values.put(KEY_stat_envoi,stat_envoi);
        values.put(KEY_stat_envoi1,stat_envoi1);
        values.put(KEY_login, login);
        values.put(KEY_pod,pod);
        values.put(KEY_service,service);
        values.put(KEY_designation1,designation);
        values.put(KEY_mode_liv,mode_liv);
        values.put(KEY_rue,rue);
        values.put(KEY_code_postal,code_postal);
        values.put(KEY_flag,flag);
        values.put(KEY_date,date);
        values.put(KEY_adr_relais,adr_relais);
        values.put(KEY_relais,relais);
        values.put(KEY_agc,agc);
        values.put(KEY_adr_agc,adr_agc);
        values.put(KEY_obj,obj);
        values.put(KEY_tel_exp,tel_exp);
        values.put(KEY_envoi_pere,code_envoi_pere);






        // Inserting Row
        long id = db.insert(TABLE_USER, null, values);


        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }








    // insert in pere1



    public void addDatapere(String envoi,String code_pere ,String size) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_PERE,envoi);
        values.put(KEY_PERE_CODE,code_pere);
        values.put(KEY_SIZE, size);

        // Inserting Row
        long id = db.insert(TABLE_pere, null, values);


        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }





    // insert dans Table_USER
    public void addData_pere(String envoi,String client,String telephone,String adresse ,String crbt, String stat_envoi,String stat_envoi1, String login,String pod,String service,String designation,String mode_liv,String rue,String code_postal, String flag,String date,String adr_relais,String relais,String agc,String adr_agc,String obj,String tel_exp,String code_pere) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_code_envoii, envoi);
        values.put(KEY_client1, client);
        values.put(KEY_telephone1, telephone);
        values.put(KEY_adresse1, adresse);
        values.put(KEY_crbt1,crbt);
        values.put(KEY_stat_envoii1,stat_envoi);
        values.put(KEY_stat_envoi11,stat_envoi1);
        values.put(KEY_login1, login);
        values.put(KEY_pod1,pod);
        values.put(KEY_service1,service);
        values.put(KEY_designation11,designation);
        values.put(KEY_mode_liv1,mode_liv);
        values.put(KEY_rue1,rue);
        values.put(KEY_code_postal1,code_postal);
        values.put(KEY_flag1,flag);
        values.put(KEY_date1,date);
        values.put(KEY_adr_relais1,adr_relais);
        values.put(KEY_relais1,relais);
        values.put(KEY_agc1,agc);
        values.put(KEY_adr_agc1,adr_agc);
        values.put(KEY_obj1,obj);
        values.put(KEY_tel_exp1,tel_exp);
        values.put(KEY_code_envoi_pere, code_pere);

        // Inserting Row
        long id = db.insert(Table_envoi_pere, null, values);


        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }



    public void addData1(String code_objet,String code_envoi,String agence,String agent,String zsd) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_code_objet, code_objet);
        values.put(KEY_code_envoi, code_envoi);
        values.put(KEY_agence, agence);
        values.put(KEY_agent, agent);
        values.put(KEY_zsd,zsd);


        // Inserting Row
        long id = db.insert(TABLE_USER1, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }



    public void addmotif(String motif,String designation) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_motif, motif);
        values.put(KEY_designation,designation);

        // Inserting Row
        long id = db.insert(TABLE_motif, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }




    public void addrelais(String relais) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_relais_des,relais);


        // Inserting Row
        long id = db.insert(TABLE_relais, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }







    public void adddivision(String divison) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_divison, divison);


        // Inserting Row
        long id = db.insert(TABLE_division, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }



    public void addmesure(String motif,String mesure,String stat) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_motiff, motif);

        values.put(KEY_mesure,mesure);
        values.put(KEY_statt,stat);

        // Inserting Row
        long id = db.insert(Table_mesure, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }




    public void addData2(String user,String pwd) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_user, user);
        values.put(KEY_pwd, pwd);



        // Inserting table Zfacteur
        long id = db.insert(TABLE_USER2, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    public boolean addData3(String code_barre,String facteur,String txt30,String txt04 ,String stsma,String statprec,String stat,String motif,String mesure ,String modepaiement,String ModeLiv,String TypePid,String Destinataire ,String Pid,String div,String relais){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_code_barre,code_barre);
        values.put(KEY_facteur,facteur);
        values.put(KEY_txt30,txt30);
        values.put(KEY_txt04,txt04);
        values.put(KEY_stsma,stsma);
        values.put(KEY_statprec,statprec);
        values.put(KEY_stat,stat);
        values.put(KEY_motif1,motif);
        values.put(KEY_mesure1,mesure);
        values.put(KEY_modeliv,ModeLiv);
        values.put(KEY_modepaiement,modepaiement);
        values.put(KEY_typepid,TypePid);
        values.put(KEY_pid,Pid);
        values.put(KEY_destinataire,Destinataire);
        values.put(KEY_division,div);
        values.put(KEY_relaiss,relais);



        long id = db.insert(TABLE_USER3, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
        return  true;
    }



    /////////////////******************INSERT FDR***********////////////


    public void addData_FDR(String num_fdr, String num_poste, String num_client,String nom_client, String Interlocuteur, String telephone, String adresse , String date, String heure, String facteur, String stat,String agent, String agence,String type_cl,String heure_tr,String code_2d,String flag,String motif,String nbenvoi) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_FDR,num_fdr);
        values.put(KEY_NUM_POST,num_poste);
        values.put(KEY_NUM_CLIENT, num_client);
        values.put(KEY_NOM_CLIENT, nom_client);
        values.put(KEY_INTERLOCUTEUR,Interlocuteur);
        values.put(KEY_TEL_CLIENT, telephone);
        values.put(KEY_Adresse_client, adresse);
        values.put(KEY_DATE_FDR,date);
        values.put(KEY_HEURE_FDR,heure);
        values.put(KEY_FACTEUR_FDR,facteur);
        values.put(KEY_stat_FDR,stat);
        values.put(KEY_agent_FDR,agent);
        values.put(KEY_agence_FDR,agence);
        values.put(KEY_type_cl,type_cl);
        values.put(KEY_heuretr,heure_tr);
        values.put(KEY_code_2D_CL,code_2d);
        values.put(KEY_FLAG,flag);
        values.put(KEY_MOTIF,motif);
        //HHA 10/06/2020
        values.put(KEY_NB_ENVOI,nbenvoi);





        // Inserting Row
        long id = db.insert(TABLE_FDR, null, values);


        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }
    /////////////////******************Recupérer le nombre d'envoi de chaque collecte ******************////////////

    public Cursor getNbEnvoiCL(String code_FDR,String client,String user) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }


        Cursor cursor = db.rawQuery("SELECT nbenvoi FROM " + TABLE_FDR + " WHERE "+ KEY_ID_FDR + " = ? AND " + KEY_NUM_CLIENT + " = ? AND " + KEY_FACTEUR_FDR + " = ? ",new String[]{code_FDR,client,user});
        return  cursor;
    }




    /*********************************Modification de la zone nb envoi collecte************************/

    public boolean updatenbenvoi(String nbenvoi,String code_FDR,String client,String user) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NB_ENVOI,nbenvoi);
        db.update(TABLE_FDR,values, KEY_ID_FDR + " = ? AND " + KEY_NUM_CLIENT + " = ? AND " + KEY_FACTEUR_FDR + " = ? ",new String[]{code_FDR,client,user});

        return  true;
    }




    /////////////////******************INSERT table client******************////////////


    public void addData_client(String id_client, String nom_client, String contact, String telephone , String adresse,String agence_id,String agence,String region,String code2D) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_client,id_client);
        values.put(KEY_NOM_CLIENTT,nom_client);
        values.put(KEY_CONTACT,contact);
        values.put(KEY_TELEPHONE_CLIENT, telephone);
        values.put(KEY_ADRESSE_CLIENT, adresse);
        values.put(KEY_ID_AGENCE_CLIENT,agence_id);
        values.put(KEY_AGENCE_CLIENT,agence);
        values.put(KEY_REGION_CLIENT,region);
        values.put(KEY_CODE_2D,code2D);

        // Inserting Row
        long id = db.insert(TABLE_client, null, values);


        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }






    /////////////////******************INSERT table envoi des collecte******************////////////


    public void addData_envoi_CL(String id_CL, String num_client, String code_envoi,String facteur) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_ENVOI_CL,id_CL);
        values.put(KEY_ID_client_ENVOI,num_client);
        values.put(KEY_code_envoi_CL,code_envoi);
        values.put(KEY_facteur_CL,facteur);

        // Inserting Row
        long id = db.insert(TABLE_ENVOIS_CL, null, values);


        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }




 /*********** insérer dans la table motif cl ********/

    public void addmotif_cl(String motif,String designation) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_motif_cl, motif);
        values.put(KEY_designation_cl,designation);

        // Inserting Row
        long id = db.insert(TABLE_motif_CL, null, values);
        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }


/********************insertion de la collecte mode offline******************/

    public void addData_CL_offline(String num_fdr, String num_poste, String num_client,String nom_client, String Interlocuteur, String telephone, String adresse , String date, String heure, String facteur, String stat,String agent, String agence,String type_cl,String heure_tr,String code_2d,String motif,String code_envoi,String nb_envoi) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ID_FDR_off,num_fdr);
        values.put(KEY_NUM_POST_off,num_poste);
        values.put(KEY_NUM_CLIENT_off, num_client);
        values.put(KEY_NOM_CLIENT_off, nom_client);
        values.put(KEY_INTERLOCUTEUR_off,Interlocuteur);
        values.put(KEY_TEL_CLIENT_off, telephone);
        values.put(KEY_Adresse_client_off, adresse);
        values.put(KEY_DATE_FDR_off,date);
        values.put(KEY_HEURE_FDR_off,heure);
        values.put(KEY_FACTEUR_FDR_off,facteur);
        values.put(KEY_stat_FDR_off,stat);
        values.put(KEY_agent_FDR_off,agent);
        values.put(KEY_agence_FDR_off,agence);
        values.put(KEY_type_cl_off,type_cl);
        values.put(KEY_heuretr_off,heure_tr);
        values.put(KEY_code_2D_CL_off,code_2d);
       // values.put(KEY_FLAG_off,flag);
        values.put(KEY_MOTIF_off,motif);
        values.put(KEY_code_envoi_off,code_envoi);
        values.put(KEY_nb_envoi,nb_envoi);






        // Inserting Row
        long id = db.insert(TABLE_CL_OFFLINE, null, values);


        db.close(); // Closing database connection

        Log.d(TAG, "New user inserted into sqlite: " + id);
    }





/**************** inserer dans la table d'objet CL**********/

    public void add_object_table(String num_objet,String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_FDR_CL, num_objet);
        values.put(KEY_user_FDR,username);




        long id = db.insert(TABLE_objet_CL, null, values);
        db.close(); // Closing database connection


    }









    /**************** inserer dans la table d'objet DNL**********/

    public void add_object_table_DNL(String num_objet,String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DNL, num_objet);
        values.put(KEY_USER_DNL,username);




        long id = db.insert(TABLE_objet_DNL, null, values);
        db.close(); // Closing database connection


    }










    public boolean updateData(String code_barre, String facteur, String txt30, String txt04, String stsma, String statprec, String stat,String raison,String motif,String mesure ,String modepaiement,String ModeLiv,String TypePid,String Destinataire ,String Pid) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_facteur,facteur);
        values.put(KEY_txt30,txt30);
        values.put(KEY_txt04,txt04);
        values.put(KEY_stsma,stsma);
        values.put(KEY_statprec,statprec);
        values.put(KEY_stat,stat);
        values.put(KEY_motif1,motif);
        values.put(KEY_mesure1,mesure);
        values.put(KEY_mode_liv,ModeLiv);
        values.put(KEY_modepaiement,modepaiement);
        values.put(KEY_typepid,TypePid);
        values.put(KEY_pid,Pid);
        values.put(KEY_destinataire,Destinataire);

        db.update(TABLE_USER3,values, KEY_code_barre+"= ?",new String[]{code_barre});

          return  true;
    }


    public void updateDataglobal(String cmd,String envoi) {


            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(KEY_stat_envoi,envoi);
            db.update(TABLE_USER,values, KEY_commande+"= ?",new String[]{cmd});


    }

    public void updatesyn(String cmd,String envoi, String obj ) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_stat_envoi,envoi);
        values.put(KEY_obj,obj);
        db.update(TABLE_USER,values, KEY_commande+"= ?",new String[]{cmd});


    }

    public void updateDataglobal2(String cmd,String envoi1) {


        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_stat_envoi1,envoi1);
        db.update(TABLE_USER,values, KEY_commande+"= ?",new String[]{cmd});


    }




    public void updateStatut(String num_client,String statut,String heuretr,String ID) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_stat_FDR,statut);
        values.put(KEY_heuretr,heuretr);
        db.update(TABLE_FDR,values, KEY_NUM_CLIENT+"= ? AND id_fdr = ?",new String[]{num_client,ID});

    }




    public HashMap<String, String> getData() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER3;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("code_barre", cursor.getString(1));
            user.put("facteur", cursor.getString(2));
            user.put("txt30", cursor.getString(3));
            user.put("txt04", cursor.getString(4));
            user.put("stsma", cursor.getString(5));
            user.put("statprec", cursor.getString(6));
            user.put("stat", cursor.getString(7));
            user.put("raison", cursor.getString(8));



        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    public void supprimer(String code) {



        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM data WHERE cmd='"+code+"'"); //delete  row in a table with the condition
        db.close();


    }


    public void supprimerAll() {



        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM data");
        db.close();





    }

    public HashMap<String, String> getData1() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER1;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {

            user.put("objet", cursor.getString(1));
            user.put("envoi", cursor.getString(2));
            user.put("agence", cursor.getString(3));
            user.put("agent", cursor.getString(4));
            user.put("zsd", cursor.getString(5));


        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }


    public HashMap<String, String> getData2() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT  * FROM " + TABLE_USER2;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("user", cursor.getString(1));
            user.put("pwd", cursor.getString(2));



        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Fetching user from Sqlite: " + user.toString());

        return user;
    }



    public Cursor getAll() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER, null);
        return  cursor;
    }

    /*************************get offline cl**************/



    public Cursor getCL_OFFLINE(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_CL_OFFLINE + " where "+KEY_FACTEUR_FDR_off + " = ?",new String[]{username} );
        return  cursor;
    }



    public Cursor getpere(String code_envoi) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT envoi_pere FROM data WHERE "+ KEY_commande + " = ?",new String[]{code_envoi});
        return  cursor;
    }


    public Cursor getAlldist(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT DISTINCT obj FROM " + TABLE_USER + " where " +  KEY_login + " = ?",new String[]{username});
        return  cursor;
    }

    public Cursor getAllDNL(String date) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER + " WHERE " + KEY_date + " = ?", new String[]{date});
        return  cursor;
    }




    public Cursor getenvoi(String obj) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE envoi='En cours' AND "+ KEY_obj + " = ?",new String[]{obj});
        return  cursor;
    }




    public Cursor getenvoiper(String obj) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM envoi_pere WHERE "+ KEY_obj1 + " = ?",new String[]{obj});

        return  cursor;
    }



    public Cursor getenvoipere(String envoi_pere) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE "+ KEY_envoi_pere + " = ?",new String[]{envoi_pere});
        return  cursor;
    }



    public Cursor getenvoipr(String envoi , String envoi_pere) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE "+ KEY_commande+ " = ? AND " + KEY_envoi_pere+ " = ? ",new String[]{envoi,envoi_pere});
        return  cursor;
    }

    public Cursor getenvoipere1(String envoi_pere) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM envoi_pere WHERE "+ KEY_code_envoi_pere + " = ?",new String[]{envoi_pere});
        return  cursor;
    }

    public Cursor getAllmotif() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_motif, null);
        return  cursor;
    }




    /* récupérer les motifs de la cl */


    public Cursor getAllmotif_cl() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_motif_CL, null);
        return  cursor;
    }







    public Cursor getAllrelais() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_relais, null);
        return  cursor;
    }


    /*****************************getALL nom  client  COLLECTE***********************************/

    public Cursor getnomclient() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT nom_client FROM " + TABLE_client, null);

        return  cursor;
    }

    /*****************************get numero client COLLECTE***********************************/

    public Cursor getnumclient(String nom_client) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT id_client FROM " + TABLE_client + " WHERE nom_client" + " = ?",new String[]{nom_client});


        return  cursor;
    }


    public Cursor getAlldiv() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_division, null);
        return  cursor;
    }







    public Cursor getAllmesure() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + Table_mesure, null);
        return  cursor;
    }


    public Cursor getAllCondition() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE ( envoi='En cours' or envoi='EN COURS') ",null);
        return  cursor;

    }

    public Cursor getAllCode(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE ( envoi='En cours' or envoi='EN COURS') AND "+ KEY_commande + " = ?",new String[]{code});
        return  cursor;
    }


    public Cursor getsize(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM data WHERE "+ KEY_envoi_pere + " = ?",new String[]{code});
        return  cursor;
    }

      /************ get nombre d'envoi des collecte*******/

    public Cursor getnbenvoi(String num_client,String ID,String facteur) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*)  FROM envois WHERE "+ KEY_ID_client_ENVOI + " = ? AND " + KEY_ID_ENVOI_CL + " = ? AND " + KEY_facteur_CL + " = ?",new String[]{num_client,ID,facteur});
        return  cursor;
    }


    public Cursor getsize1(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM pere WHERE "+ KEY_PERE_CODE + " = ?",new String[]{code});
        return  cursor;
    }

/********* get Statut de la collecte*********/


    public Cursor getLIV(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE (envoi='livré' AND "+ KEY_commande + " = ?) OR (envoi='Non livré' AND "+ KEY_commande + " = ?)" ,new String[]{code});
        return  cursor;
    }



    public Cursor getmode_liv(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT mode_liv FROM data WHERE "+ KEY_commande + " = ? " ,new String[]{code});
        return  cursor;
    }



    public Cursor getLIV1(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE (envoi='livré' OR envoi='Non livré' OR  envoi='Avisé') AND "+ KEY_commande + " = ?"  ,new String[]{code});
        return  cursor;
    }




    public Cursor getAllobj(String obj) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE envoi='En cours' AND "+ KEY_obj + " = ?",new String[]{obj});
        return  cursor;
    }


    public Cursor getobj(String obj) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT cmd FROM data WHERE "+ KEY_obj + " = ?",new String[]{obj});
        return  cursor;
    }


    public Cursor getper(String code) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM pere WHERE "+ KEY_PERE + " = ?",new String[]{code});
        return  cursor;
    }




    public Cursor getmotif(String designation) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_motif+ " WHERE " + KEY_designation + " = ?", new String[]{designation});
        return  cursor;
    }





    public Cursor getmesure(String motif) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + Table_mesure+ " WHERE " + KEY_motiff + " = ?", new String[]{motif});
        return  cursor;
    }



    public Cursor getSatmesure(String designation) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + Table_mesure+ " WHERE " + KEY_mesure + " = ?", new String[]{designation});
        return  cursor;
    }




    public Cursor getAllCondition2() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE envoi='En cours' AND envoi1='LOT'  ",null);
        return  cursor;
    }

    public Cursor getFlag(String code_envoi) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE "+  KEY_commande + " = ?",new String[]{code_envoi});
        return  cursor;





    }
    public Cursor getUserCmd2(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }

        Cursor cursor = db.rawQuery("SELECT * FROM data WHERE envoi='En cours' AND envoi1='LOT' AND "+  KEY_login + " = ?",new String[]{username});
        return  cursor;





    }



    public Cursor getAll3(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER3+" where "+KEY_facteur + " = ?",new String[]{username});
        return  cursor;
    }

    public Cursor getAl() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER3,null);
        return  cursor;
    }

    public Cursor getAll1() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER1, null);
        return  cursor;
    }
    public Cursor getAll2() {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER2, null);
        return  cursor;
    }
//////////////////***********getFDR***************/////////////////////////


    public Cursor getFDR(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_FDR +" where "+ KEY_FACTEUR_FDR + " = ? and flag = 0",new String[]{username});
        return  cursor;
    }




    public Cursor getFDRClient(String username,String num_client,String ID_FDR) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT num_client FROM " + TABLE_FDR +" where "+ KEY_FACTEUR_FDR + " = ? AND " +  KEY_NUM_CLIENT + " = ? AND " + KEY_ID_FDR + " = ?",new String[]{username,num_client,ID_FDR});
        return  cursor;
    }







/********************recupérer le statut de la FDR*************/




public Cursor getFDRStatut(String num_client,String ID ) {
    SQLiteDatabase db = this.getReadableDatabase();
    if (db == null) {
        return null;
    }

    Cursor cursor = db.rawQuery("SELECT * FROM FDR WHERE "+ KEY_NUM_CLIENT + " = ? AND id_fdr =? AND (stat='Non Traité' OR  stat='Traité')" ,new String[]{num_client,ID});

    return  cursor;
}

 /********* récuperer les ID de la FDR ************/



 public Cursor getID(String ID) {
     SQLiteDatabase db = this.getReadableDatabase();
     if (db == null) {
         return null;
     }
     Cursor cursor = db.rawQuery("SELECT num_client FROM FDR  WHERE "+ KEY_ID_FDR + " = ?",new String[]{ID});
     return  cursor;
 }






    public Cursor getAllIDFDR(String ID) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT * FROM FDR WHERE stat='En cours' AND "+ KEY_ID_FDR + " = ?",new String[]{ID});
        return  cursor;
    }



    public Cursor getALLFDR(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT DISTINCT id_fdr FROM " + TABLE_FDR + " where " +  KEY_FACTEUR_FDR + " = ?",new String[]{username});
        return  cursor;
    }



    public Cursor getenvoicl(String ID,String num,String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT " +KEY_code_envoi_CL + " FROM envois where " +  KEY_facteur_CL+ " = ? AND " + KEY_ID_ENVOI_CL + " = ? AND " + KEY_ID_client_ENVOI + " = ? "  ,new String[]{username,ID,num});
        return  cursor;
    }




    public Cursor getCL(String facteur) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        String req = "SELECT * FROM envois a INNER JOIN FDR b ON a.id_cl_envoi=b.id_fdr AND a.id_client=b.num_client  WHERE a.facteur =?  AND b.flag = 1";
        Cursor cursor = db.rawQuery( req,new String[]{facteur});
        return  cursor;
    }



    /*********** récuperer les numero d'objet de la collecte******************/


    public Cursor getobject_cl(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT id_fdr FROM " + TABLE_objet_CL + " where " +  KEY_user_FDR + " = ?",new String[]{username});
        return  cursor;
    }



    /*********** récuperer les numero d'objet de la collecte******************/


    public Cursor getobject_dnl(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT "+KEY_DNL +" FROM " + TABLE_objet_DNL + " where " +  KEY_USER_DNL + " = ?",new String[]{username});
        return  cursor;
    }



    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteobj(String obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, KEY_obj + " = ?",new String[]{obj});
        db.close();

    }
    /*BTR-32*/
    public void deleteobjsync(String obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER, KEY_obj + " != ?",new String[]{obj});
        db.close();

    }



    public void deleteenvois(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ENVOIS_CL, KEY_ID_ENVOI_CL + " = ?",new String[]{ID});
        db.close();

    }





    public void deleteIDCL(String ID) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_FDR, KEY_ID_FDR + " = ?",new String[]{ID});
        db.close();

    }



    /**************** recupérer nombre envois offline**********/
    public Cursor get_nb_envoi_offline(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM "+ TABLE_CL_OFFLINE+" WHERE "+ KEY_FACTEUR_FDR_off + " = ? AND "+ KEY_code_envoi+ " !=null",new String[]{username});
        return  cursor;
    }



    /****************************clear CL offline table ***************/






    public void delete_offline_CL(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_CL_OFFLINE, KEY_FACTEUR_FDR_off + " = ?",new String[]{username});
        db.close();

    }



    public void deletecode(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER3, KEY_code_barre + " = ?",new String[]{code});
        db.close();

    }


    public void deleteenvoi(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER3, KEY_facteur + " = ?",new String[]{username});
        db.close();

    }


    /*******************delete offline CL**************/





    public void deleteoffline(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows


        String req = "DELETE  FROM envois WHERE id_cl_envoi in ( SELECT id_cl_envoi FROM envois a INNER JOIN FDR b ON a.id_cl_envoi=b.id_fdr AND a.id_client=b.num_client  WHERE a.facteur =?  AND b.flag = 1)";

       db.beginTransaction();
        try {
            db.delete(TABLE_FDR, KEY_facteur + " = ? AND " + KEY_FLAG + " = 1", new String[]{username});
            db.execSQL(req,new String[]{username});
            db.setTransactionSuccessful();
        }catch(Exception e){
            //end the transaction on error too when doing exception handling
           db.endTransaction();
            throw e;
        }
        db.endTransaction();

    }

    public void deletediv() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows

        db.delete(TABLE_division, null, null);


        db.close();

    }

    public void deleteper(String code) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_pere, KEY_PERE + " = ?",new String[]{code});
        db.close();

    }

    public void deletepere(String code_pere) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(Table_envoi_pere, KEY_code_envoi_pere  + " = ?",new String[]{code_pere});
        db.close();

    }




    public void delete_envoi(String code_envoi) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_ENVOIS_CL, KEY_code_envoi_CL  + " = ?",new String[]{code_envoi});
        db.close();

    }






    public void deletepere1(String obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(Table_envoi_pere, KEY_obj1  + " = ?",new String[]{obj});
        db.close();

    }





    public void deleteData1() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER1, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }


    public void deletepr() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_pere, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }

    public void deleteData2() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER2, null, null);
        db.close();

        Log.d(TAG, "Deleted all user info from sqlite");
    }
    public void deleteData3() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_USER3, null, null);
        db.close();


        Log.d(TAG, "Deleted all user info from sqlite");
    }

    /************supprimer les num d'objet collecte**************/



    public void deleteobject(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        String req = "DELETE FROM objet_cl WHERE facteur  =? ";

        db.execSQL(req,new String[]{username});

    }


    /************supprimer les num d'objet DNL**************/



    public void deleteobject_dnl(String username) {

        SQLiteDatabase db = this.getWritableDatabase();

        String req = "DELETE FROM objet_dnl WHERE facteur  =? ";

        db.execSQL(req,new String[]{username});

    }






    public void updaterasion(String code_barre,String raison,String motif ,String mesure) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_motif1,motif);
        values.put(KEY_mesure1,mesure);
        db.update(TABLE_USER3,values, KEY_code_barre+"= ?",new String[]{code_barre});



    }
    public Cursor getUserCmd(String username) {
        SQLiteDatabase db = this.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery("SELECT  * FROM " + TABLE_USER + " WHERE " + KEY_login + " = ?", new String[]{username});
        return  cursor;
    }

//////////////////**************Delete FDR***********//////////////////////


    public void deleteCollecte(String username) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(TABLE_FDR, KEY_FACTEUR_FDR + " = ?",new String[]{username});
        db.close();

    }







}