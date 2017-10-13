package com.example.rahul.myotpgeneration;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by VNurtureTechnologies on 05/09/16.
 */
public class Utils {
    // pump name
    public static String[] pumpName = new String[]{"PETROLEUM & MINERALS (MKT) (PHULE MKT)","THE NATIONAL PETROL CO","VARMA AUTOMOBILES","J.B.PATEL & CO ( QUEENS RD)","EXPRESS PETROLEUMS","GLOBAL PETROLEUM","LIBERTY AUTO","PETROLEUM & MINERALS (T) (TARDEO)","RAJ AUTOMOBILES_2","TAJ AUTO","UNITED MOTORS","AMAR AUTOMOBILES.. (WADI BUNDER)","J SINGH BROTHERS G/P","JANATA SERVICE STATION","SETHI AUTOMOBILES","UNIVERSAL MOTORS","KAUSAR AUTO SERVICE","SHETKARI SAHAKARI SANGH LTD.","SHETTY AUTO DADAR","TAXI MENS DADAR","GILL AUTO SERVICE","KENIA AUTOMOBILES","LAXMI  ENTERPRISES","SHAHEED BAPURAO DHURGUDE AUTOWORK","BHARUCHA AUTO SERVICE","PARK GARAGE","WORLIWAY SERVICE STATION-II","AMAR AUTOMOBILES (A/C MAHESHWAR)","COCO BP CHARCHGATE","EXPRESS SERVICES","DESAI AUTO SERVICE","RAVI AUTO SERVICES","NEW KAMPALA SERVICE STATION","NOBLE MOTORS SERVICE","BOMBAY PETROLEUM","NATIONAL PETROL COMPANY,DADAR","SATGURU AUTOMOBILES","UGANDA SERVICE STATION","DAYARAM SANTADAS & CO","DAULAT AUTOMOBILES","DIPTI PETROLEUM","GATEWAY AUTO SERVICE","NEW BOMBAY AUTO SUPPLY","TAXIMAN SERVICES LTD","BP-BKC","ADARSH SERVICE STATION","M/S OM SIDDHESHWARI FUEL WORLD","PRAKASH AUTOMOBILES_2","VIKRAM AUTO SERVICE","BAJRANG AUTO SERVICE","GURUNANAK AUTOMOBILES","JOGESHWARI PETROL SUPPLY CO.","GOREGAON PETROL SUPPLY CO","PATEL AUTOMOBILES_2","JAMAL SINGH AUTO","BP-KANDIVILI","CHARKOP PETROLEUM","DASHMESH AUTO","AURO PETROLEUM CENTRE","BAHRI AUTO SERVICE","K M SUCHAK","RADIANT PETROLEUM PROD.PVT.LTD.","SHAH PETROLEUM PRODUCTS","ABHISHEK SERVICE CENTER","BHAMINT ENTERPRISES","CHHEDA SERVICE STATION","KAKA AUTOMOBILES","MAHAVIR AUTO","GODREJ & BOYCE","VIKHROLI AUTOMOBILES","HIGHWAY AUTOMOBILES, GHATKOPAR","BORIVALI  PETROL SUPPLY CO","FAMOUS AUTO SERVICE STATION","OM VEDANT PETROLEUM","LAKHBIR AUTOMOBILES","MASTAKAR AUTO SERVICE STATION","OM LALITA PETROLEUM","GANESH AUTOMOBILES","UNIVERSAL SERVICE CENTRE","BP - PADGAH","LAXMI AUTOMOBILES","RAVI AUTO SERVICES(2)","B P MAHAPE","DASHMESH AUTO_2","B P SANPADA","SIDDHIVINAYAK FUEL CENTRE","UNIVERSAL AUTO","DAHISAR SERVICE STATION","BASSEIN PETROL SUPPLY CO","BHARAT PETROL SUPPLY CO","PANKAJ AUTO CENTRE","YASH SERVICE CENTER","C.T. PARIKH S C","AMBOLI FUEL CENTRE","C P SHAH (TALASIRI)","A G JAJAL","AMOL","CENTUARY FEUL CENTRE","GURUGOBIND MOTORS","V. K. MISHRA","KRISHNA PETROLEUM ( KALAMBOLI)","DASHMESH ENTERPRISE","RAVI AUTO SERVICES (ADHOC)","ROSHAN AUTO","CHANDAN S STATION","OM VINAYAK PETROLEUM","SUNDERLAL BENIPRASAD","MAHALAXMI TRANSPORT","AGARWAL TRANSLINK",
            "PREMIER SUPPLY CO.", "ADARSH AUTOMOBILES", "BRIGHT STAR", "MEHROFI TRADERS", "PREMIER SUPPLY CO.", "SHRI SADGURU SERVICE STATION", "PRAJAKTA PETROLEUM", "DESAI & CO", "V B PAREKH", "ADARSH AUTOMOBILES", "DESHMUKH PETROLEUM", "MEHROFI TRADERS", "PREMIER SUPPLY CO.", "SELECT AUTOMOBILES", "SHOLAPUR MOTORS", "KOTESHWAR PETROLEUM", "NAMJOSHI PETROLEUM", "PREMCHAND LAXMICHAND AND SONS", "KEDARI SERVICE STATION", "MAGAR PETROLEUM, MUNDHWA , DIST-PUNE", "SHREE SERVICE STATION", "SHREE SIDDHIVINAYAK_EWAY", "JAY SHAMBHO PETROLEUM", "KODSHI HIGHWAYS", "RELIABLE PETROLEUM", "ISHWAR SERVICE STATION", "EXPRESS PETRO SERVICES", "RELIABLE PETROLEUM", "EXPRESS PETRO SERVICES", "BHARAT PETROL DEPOT (NAVI PETH)", "MITALI SERVICE STATION", "SIDDHANT PETROLEUM", "BHARAT PETROL DEPOT..(TILAK RD)", "BP COCO EXPRESS WAY", "JOHN PEREIRA, KATURE VASTI", "MP AUTOMOBILES", "MP AUTOMOBILES", "ROSHNI SERVICES", "FAMOUS AUTO CENTRE", "SHREE SIDDHIVINAYAK_EWAY", "KOTESHWAR PETROLEUM", "D J PETROLEUM", "ASHISH PETROLEUM", "RAJLAXMI SERVICE CENTRE", "MILAN PETROLEUM", "BP PUNE - 1", "NANDKUMAR & BROS", "SHUBHAM PETROLEUM", "BANDAL PETROLEUM", "SWARAJ PETROLEUM", "LAKSHMI PETROLEUM", "KANCHAN SERVICE CENTRE", "SHOLAPUR MOTORS", "MOHANISH AUTO", "DESHMUKH PETROLEUM", "BP KHEDSHIVAPUR", "BHARAT FUEL CENTRE", "TAWARE PETROLEUM AGENCY", "PREMIER SUPPLY CO.", "KASAT PETROLEUM", "SELECT AUTOMOBILES", "RAJYOG PETROLEUM ", "YASH PETROLEUM", "KISAN BORREWELL CO", "RAJENDER FUEL CENTER", "LOBH PETROLEUM", "BHARAT PETROL DEPOT..(TILAK RD)", "BANDAL PETROLEUM", "UP GUJAR", "SHUBHAM PETROLEUM", "RAJENDER FUEL CENTER", "PREMCHAND LAXMICHAND AND SONS", "KADAM PETROLEUM", "SIDDHIVINAYAK PETROLEUM", "JAI HIND HIGHWAY SERVICE STATION", "BP KELEWADI", "MAULI PETROLEUN", "MOHANISH AUTO", "SIDDHIVINAYAK PETROLEUM", "LOBH PETROLEUM", "PATEL SERVICE STATION", "BHARAT PETROL DEPOT (NAVI PETH)", "ATUL SALES & SERVICES", "MILAN PETROLEUM", "DESAI & CO", "M/S MAHI PETRO TRADERS, JEJURI", "DIGAMBER PETROLEUM", "NANA DHUMAL PETROLEUM", "SAHYADARI SERVICE STATION", "DHARMAWAT PETROLEUM", "JANATA AUTOMOBILES (PUMP)", "D.G.MUTHA", "NANA DHUMAL PETROLEUM", "SANDIP PETROLEUM", "SHREEPAD HIGHWAY FUEL CENTRE", "SAI PETROLEUM, TALEGAON DHAMDARE", "SIDDHIVINAYAK PETROLEUM", "V B PAREKH", "SHAH DARSHI JEEVAN ( BHIGWAN)", "KASAT PETROLEUM", "SHREE RAMCHANDRA SERVICE STN.", "BHARAT PETROL DEPOT (NAVI PETH)", "ATUL SALES & SERVICES", "SHREEPAD HIGHWAY FUEL CENTRE", "M/S DEV PETROLEUM", "SHUBHAM PETROLEUM, BHIGWAN", "ASHOK PETROLEUM", "DATTA AUTOMOBILES", "YASHRAJ PETROLEUM", "SHERU PETROLEUM", "AVADAI PETROLEUM", "BP COCO EXPRESS WAY", "VARDHAMAN PETROL DEPO", "WTA KATRAJ", "DHAWADE PETROLEUM STATION", "M/S SHREE GANESHA PETROELUM", "KAWADE PETROLEUM", "BP PUNE - 1", "SANDIP PETROLEUM", "M/S DIGVIJAY PETROLEUM", "AMIT SERVICE STATION", "M/S MAHI PETRO TRADERS, JEJURI", "DESAI & CO", "ALFA SERVICE STATION (NAIGAON)", "M/S. BORAWAKE PETRO", "M/S. HARIOM PETRO", "M/S. SHIVRAJ PETRO", "M/S DHRUV PETROLEUM", "SWAMI PETROLEUM", "SHIVSHAKTI PETROLEUM", "NEELKANTHA PETROLEUM", "BELDARE PETROLEUM", "S G DATE AND CO.", "VIJAY PETROLEUM KHANDALA", "ALFA SERVICE STATION (WAKDEWADI)", "V.B. PAREKH", "R M DESAI", "IRANI PETROL PUMP", "M/S AARUJ PETROLEUM", "M/S SHRI SAI PETROLEUM", "J P PETROLEUM", "MEERA PETROLEUM SIRUR", "RAJ AUTOMOBILES", "S G DATE AND CO.", "ALFA SERVICE STATION (WAKDEWADI)", "KEDARI SERVICE STATION", "NAMJOSHI PETROLEUM", "PATEL SERVICE STATION", "DHARMAWAT PETROLEUM", "ISHWAR SERVICE STATION", "M/S WANDHEKAR PETROLEUM", "MAULI PETROLEUN", "RAGHUNANDAN PETROLEUM ", "SATHE PATIL PETROLEUM", "VINAYAK PETROLEUM", "M/S SHUBAM PETROL DEPOT (ADHOC)", "YASH PETROLEUM PUNE ", "MAG PETRO", "UP GUJAR", "SHREE SERVICE STATION", "DESAI & CO", "M/S SHITOLE PETROLEUMS", "M/S SHEVANTAI PETROLEUM", "JAY SHAMBHO PETROLEUM", "D J PETROLEUM", "BP KHEDSHIVAPUR", "BP KELEWADI", "NUSSERWAN PETROL SERVICE", "SARASWATI AUTO", "VARDHAMAN PETROL DEPO", "SWARAJ PETROLEUM", "D.G.MUTHA", "WTA KATRAJ", "SARASWATI AUTO", "M/S VARAD PETROLEUM", "M/S J P DIVEKAR & SONS", "JAY GANESH PETROLEUM", "SHREE SAI PETROLEUM ", "HUTATAMA RAJGURU", "NUSSERWAN PETROL SERVICE", "AZAD PETROLEUM", "NM MORE", "RAHUL PETROLEUM", "TEKAWADE SERVICE STATION", "EKVIRA PETROLEUM", "BHARAT FUEL CENTRE", "SHRI NAGESHWAR PETROLEUM", "TULJABHAVANI PETROLEUM", "NANDKUMAR & BROS", "M/S DHRUV PETROLEUM", " CHANDRASHEKHAR PETROLEUM ", "MITALI SERVICE STATION", "DEVENDRA AUTOLINES", "BALAJI PETROLEUM", "BANDAL PETROLEUM", "BABJI PETROLEUM", "DEVENDRA AUTOLINES", "TRIMURTHY PETROLEUM", "KODSHI HIGHWAYS", "RAJYOG PETROLEUM ", "SAI GAURI PETROLEUM", "TRIMURTHY PETROLEUM"
    };
    public static String[] pumpAddress = new String[]{"JN OF MOHAMMED ALI; CARNAC BRIDGE, M. PHULE MARKET","41, QUEENS ROAD, M KARVE ROAD","OPP. MASJID BRIDGE, FRERE ROAD, 186, P DMELLO ROAD","QUEENS ROAD","73, S.B.SINGH ROAD,","128,WODEHOUSE ROAD,N.PARIKH MARG","115 COLABA CAUSEWAY, COLABA, S B SINGH ROAD","CABINET HOUSE, 244 TARDEO ROAD","323, M.SHAUKAT ALI ROAD","BHADKAMKAR MARG, 90, ","38, N S PATKAR MARG","P.DMELLO ROAD, WADI  BUNDER","JN OF GUNPOWDER-REAY ROAD, P DMELLO ROAD","VICTORIA ROAD BRIDGE, DARUKHANA","116, ","211, DR.M.ROAD, MAZGAON","MODERN MILLS COMPOUND, S.G.ROAD, ","JERBAI WADIA ROAD, ","TATA OIL MILLS COMPOUND, 186, BABASAHEB AMBEDKAR ROAD","14, GOKULDAS PASTA ROAD","COTTON GREEN","1ST AVENUE BOUNDARY ROAD, NEAR COTTON GREEN RLY. STN","SIGNAL HILL AVENUE, BRICK BUNDER","BPCL DEALER SEWREE FORT ROAD BEHIND C LUBE  ","JN. JAMSHEDJI ROAD, ","JN.OF VEER SAVARKAR MARG, MAHIM","PLOT NO.87, DR.ANNIE BESANT ROAD, WORLI","KING&apos;&apos;S CIRCLE, PLOT NO.362,","89, CCI, Veer Nariman Marg, ","107-A, QUEENS ROAD, M.KARVE ROAD","29, SION CIRCLE","OPP. TEXMACI, PLOT NO 271, DR. ANNIE BESANT ROAD","13 C ,","HORNBY, VELLARD, ","OPP. KOHINOOR MILLS NO.3, R.G.GADKARI CHOWK, ","JN.OF GOKHALE ROAD NORTH, ASH LANE, ","SEWREE-WADALA RP AD, 711, KATRAK ROAD,","44, RAFI AHMED KIDWAI ROAD, ","CORNER OF OOMER PARK, BHULABHAI DESAI ROAD","PLOT NO 42, RAMJIBHAI KAMANI MARG, ","3,RAMJIBHAI KAMANI MARG,","CHA.SHIVAJI MAHARAJ MARG, BEHIND REGAL CINEMA","PLOT NO.241, V.PATEL ROAD, LINKING ROAD, ","JN. OF S.V.ROAD/HILL ROAD","B.P.BKC, PLOT NO   PP-2, G-TEXT, BLOCK 6, BANDRA-KURLA COMPLEX","NEHRU ROAD,","NEAR ANDHERI FIRE BRIGADE, S.V.ROAD, ","JVPD SCHEME, NEAR BEST BUS DEPOT, ","JUHU-VERSOVA LINK ROAD, ","ANDHERI-KURLA ROAD, MAROL NAKA, ","KURLA-SUREN ROAD JNCN","JOGESHWARI (W)","JNCN.OF S.V.ROAD/AAREY ROAD, ","S.V.ROAD (BHARAT INDUS. COMPOUND)","PLOT NO. S 84, INFRONT OF ACC, L B S MARG","MHADA PLOT NO 36, DP ROAD, MAHAVIR NAGAR, ","CHARKOP INDUSTRIAL AREA, SECTOR 6, ","NEAR KAMANI ESTATE , LBS MARG","NEAR DIAMOND GARDEN, ","OPP.DIAMOND GARDEN, SION-TROMBAY ROAD","THANE BELAPUR ROAD,NEXT TO GHANSOLI RLY STATION","CHEMBUR NAKA, SION-TROMBAY ROAD, ","GHATKOPAR-MAHUL ROAD,","ABHISHEK SERVICE CENTER","BPCL CHEMBUR COLONY AZIZ BAUG ","L B SHASTRI MARG, ","JANATA MARKET, AGRA ROAD, ","S-86, PLOT NO.3, SHASTRI MARG,","PIROJSHA NAGAR, VIKROLI, LBS MARG","AGRA ROAD - (SHASTRI MARG), POWAI ROAD JN., ","HIGHWAY AUTOMOBILES, GHATKOPAR","BORIVALI  PETROL SUPPLY CO","S.V.ROAD, KAPADIA BAUG, PLOT NO.1, SURVEY NO.7","BPCL DEALER, OPP MATHOSHREE CLUB, JOGESHVARI-ANDHERI -VIKHROLI- LINK RD, NEAR KAMAL AMROHI STUDIO AN","CST ROAD, KALINA, S.G.BARVE MARG","A-14, NEW LINKING ROAD, OSHIWARA","VILL - EKSAR, NEW LINK ROAD","BPCL DEALER, AGRA ROAD, OPP MANORPADA, LSB MARG","BPCL DEALER, MAJIWADA, THANE 400601","NH - 3, MUMBAI - AGRA ROAD, VAHULI, BHIWANDI","S.V.ROAD, GHODBUNDER RD, CHITALSAR,MANPADA,THANE, MAHARASHTRA","OLD NH 4, AT POST PIMPRI, NEAR SHILPHATA,DIST.THANE","BP MAHAPE BHARAT PETROLEUM CORPORATION LTD PLOT NO X-5/4 MAHAPE NAVI MUMBAI","MUMBAI PUNE HIGHWAY, OPP. SANPADA RLY STATION, TURBHE VASHI, DIST. THANE","BPCL COCO Retail Outlet Plot No.17 Sector 24 Juinagar Sanpada Navi Mumbai Thane","PALM BEACH ROAD,SECTOR 6/24 NERUL NAVI MUMBAI","PLOT NO.6, SECTOR 13, KOPARKHARANE, NEW MUMBAI","S. V. ROAD, KASHMIRA","AGASHI RAOD,","BASSEIN TOWN","MANOR, N H 8, DIST, THANE","NH 8, VILLAGE MANOR,","OPPS. TAPS COLONY, BOISAR TARAPUR RAOD,","BHARAT PETROLEUM DEALERS GAT NO. 22/1/1, AMBOLI MUMBAI - AHMEDABAD HIGHWAY,NH8 ","TALASHRI, CHAR RASTA, DIST. THANE","PALASPHATA, MUMBAI PUNE HIGHWAY, PANVEL, DT. RAIGAD","SHEDUNG, TALUKA PANVEL, DIST. RAIGAD","B P C L DEALER,  SURVEY NO.97/A, SHIRDONE, ","MUMBAI PUNE ROAD, NH . 8, AT NADHAL, ,DIST. RAIGAD","1741, VILLAGE DHANSAR,  OLD MUMBAI PUNE ROAD","P2 KALAMBOLI TRUCK TERMINAL, NEAR LIBRA WEIGH BRIDGE,DIST. RAIGAD","VILLAVE KUKSE BORIVALI, POST. PADGHA, BHIWANDI,THANE","Manpada, Thane MAHARASHTRA","SURVEY NO 128/1 TO 5, MURBAD ROAD,","VANJARPATI NAKA, NEHRU CHOWK, AGRA ROAD","SURVEY NO. 52/2/1, VILLAGE WARRET, TAL. - BHIWANDI, AMBADI NAKA, DIST. - THANE","BOMBAY AGRA ROAD, ","SURVEY NO. 284/4 P, WADA BHIWANDI ROAD, NEAR KOKA KOLA COMPANY, AT POST KUDUS, TAL. WADA, DIST. THAN","SURVEY NO. 36. ASANGAON, NEAR RUKMA UDYOG NAGAR,, DIST - THANE",
            "LAXMI ROAD", "VADGAONSHERI", "CANNAUGHT ROAD", "GANSHKIND ROAD", "LAXMI ROAD", "JANGLI MAHARAJ ROAD", "LONI STATION, AT POST LONI KALBHOR, ", " POST-TARADGAON, MAHAD-PANDHARPUR HIGHWAY, ", "NAGEWADI, ", "VADGAONSHERI", "SATARA ROAD", "GANSHKIND ROAD", "LAXMI ROAD", "KARVE ROAD", "SHIVAJI NAGAR", "S. NO.81, KALEWADI MAIN RD., ", "RING ROAD,PHALTAN,", "AT POST KHEDGAON, CHAUFULLA", "S.NO.65 / 1+3, PLOT NO.4, ", "OPP MAGARPATTA CITY, MUNDHWA BYPASS, ", "PUNE-AHMEDNAGAR, ", "PUNE MUMBAI BY PASS", "DEALERS BHARAT PETROLEUM, 211/3, NEXT TO IBM SP INFOCITY, PUNE SASWAD ROAD, ", "284, NH4, SHIVDE", "PLOT NO.357, PUNE SOLAPUR HW", "PL.NO 83, LULLANAGAR ", "OPP. CME GATE, NR. ARUN TALKIES, OLD PUNE MUMBAI HW, ", "PLOT NO.357, PUNE SOLAPUR HW", "OPP. CME GATE, NR. ARUN TALKIES, OLD PUNE MUMBAI HW, ", "NAVI PETH", "VITHALWADI", "NAVRE PHATA", "TILAK ROAD", "AT POST PARANDWADI ", "AT POST: KATUREVASTI", "PUNE-BANGALORE HIGHWAY,", "BHARAT PETROLEUM DEALERS,PUNE-BANGALORE HIGHWAY, ", "KOREGAON PARK", "FERGUSION COLLEGE ROAD", "PUNE MUMBAI BY PASS, ", "S. NO.81, KALEWADI MAIN RD., ", "BHARAT PETROLEUM RETAIL OUTLET , KONDHWA BUDRUK, ", "A/P MOREGAON, TAL BARAMATI", "CHANDOLI", "BHARAT PETROLEUM DEALERS,", "OPP.R.T.O., 40/41, ", "8/10, KHED MAHULLI", "CHAKAN TALEGAON ROAD,", "PUNE NAGAR RD., LONIKAND, ", "At Post Hinjewadi Phase II", "At Post Hinjewadi Phase II", "EAST STREET", "SHIVAJI NAGAR", "BUND GARDEN ROAD", "SATARA ROAD", "NH-4 , VARVE ", "PUNE PAUD RD.", "MALEGAON", "LAXMI ROAD", "KASAT PETROLEUM", "KARVE ROAD", "RAVET BRT, WALHEKARWADI,", "MUNDHAWA-PUNE ROAD", "SATARA", "LONI KALBHORE", "BHUINJ", "TILAK ROAD", "PUNE NAGAR RD., LONIKAND, ", "AT &amp; POST-SHIKRAPUR, PUNE-NAGAR HIGHWAY, ", "CHAKAN TALEGAON ROAD, ", "LONI KALBHORE", "AT POST KHEDGAON, CHAUFULLA, ", "KADAM PTEROLEUM,BPCL DEALER, ", "S NO.85/1, OPP. MANJRI STUD FARM,", "CHINCHWAD", "NH-4, PUNE BANGALORE HW, ", "SURVEY NO 274, BANER ROAD,", "BUND GARDEN ROAD", "S NO.85/1, OPP. MANJRI STUD FARM, PUNE SOLAPUR HIGHWAY", "LOBH PTEROLEUM,BPCL DEALER, ", "MARKETYARD KARAD", "NAVI PETH", "CHAKAN - SHIKRAPUR ROAD, GAT NO. 453/5, PIMPALE JAGTAP, ", "BHARAT PETROLEUM DEALERS,", "AT POST NIRA, OPP S.T. STAND, ", "At Post Jejuri, ", "SHIKRAPUR-RANJANGAO ROAD", "SASWAD JEJHURI ROAD, SURVEY NO 125/2, IN FRONT OF PWD GUEST HOUSE,", "NIGADE, NH4", "KONDHAWA ", "NANAN PETH", "AT POST SHIRUR(GHODNADI) ", "SASWAD JEJHURI ROAD, SURVEY NO 125/2, IN FRONT OF PWD GUEST HOUSE,", "PLOT NO D 51/1/1,", "GUT. NO. 349/348/1, NH-9, PUNE SOLAPUR HIGHWAY", "Talegaon Dhamdare on SH 60", "S NO.85/1, OPP. MANJRI STUD FARM", "NAGEWADI", "BHIGHWAN TOWN,NH9,", "Survey No 153, Karve Road", "BHIMA KOREGAON", "NAVI PETH", "CHAKAN - SHIKRAPUR ROAD, GAT NO. 453/5, AT & POST PIMPALE JAGTAP", "GUT. NO. 349/348/1, NH-9, PUNE SOLAPUR HIGHWAY , ", "Gat No 959 MIDC Kandli ", "BHIGWAN TO RASHIN ROAD", "AT POST YAWAT", "NH-4, KHEDSHIVAPURI", "At Post - Nimgaon-Ketki", "Gut 820, Pune Solapur Road", "LHS AT WADGAON MAVAL, ", "AT POST PARANDWADI ", "WARJE", "MUMBAI - BANGLORE HIGHWAY", "KONDWE DHAWADE, ", "Ranjangaon, ", " B T Kawade Road, ", "OPP.R.T.O., 40/41, ", "PLOT NO D 51/1/1,", "Gat no: 50/4/1, Plot no 2, Baramati Town", "UTROULI VILLAGE, BHOR", "At Post Jejuri, ", "AT POST NIRA, OPP S.T. STAND, ", "1008/1, NAIGAON, ", "A/P- SOMANTHALI", "269/8/B, DHANGARWADI, TAL : KHANDALA, ", "A/P: PIMPRAD", "A/P KHODAD, PUNE BANGALORE HIGHWAY, ", "AT POST WATHAR STATION,", "PUNE SATARA RD, LONAND ", "SURVEY NO. 2 B, PHALTAN DAHIWADE RD, AT POST ZIRAPWADI,", "SR NO 35, DATTA NAGAR", "AT POST BARAMATI", "A/P: SHINDEWADI, TAL : KHANDALA,", "WAKDEWADI", "NAGEWADI,", "AT POST KOREGAON, ", "AT POST MAHABALESHWAR MAIN ROAD", "PENCIL CHOWK", "TAL BARAMATI ", "AMBAWADE, ", "SIRUR PHATA, ", "POWAI NAKA, ", "AT POST BARAMATI, ", "WAKDEWADI", "S.NO.65 / 1+3, PLOT NO.4, KEDARI NAGAR", "RING ROAD,PHALTAN", "MARKETYARD ", "KONDHAWA - PISOLI ROAD", "PL.NO 83, LULLANAGAR ", "SASWAD", "SURVEY NO 274, BANER ROAD,", "TATHAWADE, PCMC", "PIMPE NILAKH", "KESNAND, ", "Lonavala NH4 ", "GODAVALE-MASHWAD RD. ", "NIMBHORE ", "POST-SHIKRAPUR, ", "POST, PUNE-AHMEDNAGAR, WAGHOLI", "POST-TARADGAON, MAHAD-PANDHARPUR HIGHWAY", "New Shangvi.", "Narayangaon ", "DEALERS BHARAT PETROLEUM, 211/3, NEXT TO IBM SP INFOCITY, ", "BHARAT PETROLEUM RETAIL OUTLET , KONDHWA BUDRUK,", "NH-4 , VARVE NEAR NATRAJ HOTEL", "NH-4, PUNE BANGALORE HW,", "DHANKAWADI", "SHANKARSHET ROAD", "WARJE", "At Post Hinjewadi Phase II", "AT POST SHIRUR(GHODNADI) ", "MUMBAI - BANGLORE HIGHWAY,", "SHANKARSHET ROAD", "SANASWADI,", "WARWAND", "Survey No: 6/1/2/2 Saswad Katraj Bypass road, at Post: Urli Devachi, Tal : Haveli", "LOHEGAON WAGHOLI ROAD", "AT POST-CHANDOLI, PUNE-NASHIK HIGHWAY", "DHANKAWADI", "AT POST KASHIL,", "At post: Maini, ", "AT POST KHANDALA, ", "HADAPSAR", "RAM TEKDI, NEAR BHAIROBA NALA ", "PUNE PAUD RD.", "AT POST MOSHI DEHU RD ", "AT POST MOSHI DEHU RD ", "8/10, KHED MAHULLI, ", "A/P KHODAD, PUNE BANGALORE HIGHWAY, ", "SAINE VILLAGE, NH - 3 MALEGAON, AT POST : KURKUMBH, ", "VITHALWADI, ", " MEDHANKAR WADI", "Opp. Market Yard,", "PUNE NAGAR RD., LONIKAND,", "AMBEGAON BUDRUK ", " MEDHANKAR WADI", "NH-4, AT POST WATHAR", "284, NH4, SHIVDE, ", "RAVET BRT, WALHEKARWADI", "OPPOSITE ALFA LAVAL,", " AT POST WATHAR"
    };
    public static String[] pumpArrayLocation = new String[]{"Crawford Mkt","Mantralaya","Masjid bandar","Marine Lines","COLABA","COLABA","COLABA","TARDEO","LAMINGTON ROAD","GRANT ROAD","BANDRA WEST","Masjid bandar","Masjid bandar","Mazgaon","REAY ROAD","Mazgaon","MAHALAXMI","PAREL","DADAR","DADAR","COTTON GREEN","SEWREE WEST","SEWREE","SEWREE East","MAHIM","MAHIM","WORLI","MATUNGA","Churchgate","Mantralaya","SION","WORLI","PEDDAR ROAD","HAZI ALI","DADAR","DADAR","WADALA","WADALA","Warden Road","BALLARD ESTATE","BALLARD ESTATE","COLABA","BANDRA (W)","BANDRA (W)","BANDRA EAST","VILE PARLE (EAST)","ANDHERI (W)","ANDHERI (W)","VERSOVA","ANDHERI (E)","ANDHERI (E)","JOGESHWARI (W)","GOREGAON (W)","GOREGAON (W)","THANE","KANDIVILI WEST","KANDIVALI ( WEST )","KURLA","CHEMBUR","CHEMBUR","GHANSOLI","CHEMBUR","CHEMBUR","CHANDIVALI","CHEMBUR","MULUND","BHANDUP","BHANDUP","VIKHROLI","VIKHROLI","GHATKOPAR","BORIVALI (W)","BORIVALI (W)","JVLR","SANTACRUZ (EAST)","JOGESHWARI (W)","BORIVALI (W) ","NMANORPADA","MAJIWADA","BHIWANDI","MANPADA","SHIL PHATA","MAHAPE","SANPADA","JUINAGAR","NERUL","KOPARKHARANE","MIRA ROAD","VIRAR (W)","VASHI","MANOR","PALGHAR","BOISAR","THANE","TALASHRI","PANVEL","PANVEL","PANVEL","KHALAPUR","PANVEL ","KALAMBOLI","BHIWANDI","Dombivali","KALYAN","BHIWANDI","BHIWANDI","BHIWANDI","WADA","SHAPUR",
            "LAXMI ROAD", "VADGAONSHERI", "CANNAUGHT ROAD", "GANSHKIND ROAD", "LAXMI ROAD", "JANGLI MAHARAJ ROAD", "TAL HAVELI, PUNE", "PHALTAN, SATARA", "SATARA", "VADGAONSHERI", "SATARA ROAD", "GANSHKIND ROAD", "LAXMI ROAD", "KARVE ROAD", "SHIVAJI NAGAR", "PIMPRI", "LAXMINAGAR", "PUNE SOLAPUR HIGHWAY", "KEDARI NAGAR, WANOWRIE", "HADAPSAR, PUNE", "WAGHOLI, PUNE- BANGALORE HIGHWAY", "PUNAWALE", "BHEKRAINAGAR, FURSUNGI", "KARAD", "VAKHARI", "BIBVEWADI - KONDWA ROAD ", "DAPODI", "VAKHARI", "DAPODI", "NAVI PETH", "SINHGAD ROAD", "NAVRE PHATA", "TILAK ROAD", "NEAR TALEGAON TOLL NAKA TALUKA:- MAVAL DIST", "MALEGAONPETH TAL - KHED", "DISTRICT - SATARA", "DISTRICT - SATARA", "KOREGAON PARK", "DYANESWAR PADUKA CHOWK", "PUNAWALE", "PIMPRI,", "BADHE CHOWK", " SHIRUR NIRA ROAD", "NH-50, PUNE ", "KANHEPHATA, PUNE", "DR.AMBEDKAR ROAD", "KHED MAHULLI", " MAHALUNGE", "TAL. HAVELI", "Taluka Mulshi", "Taluka Mulshi", "EAST STREET", "SHIVAJI NAGAR", "BUND GARDEN ROAD", "SATARA ROAD", "NEAR NATRAJ HOTEL", "BHUKUM", "PUNE", "LAXMI ROAD", "KASAT PETROLEUM", "KARVE ROAD", "PCMC,PUNE", "MUNDHAWA-PUNE ROAD", "SATARA", "LONI KALBHORE", "DIST SATARA", "TILAK ROAD", "TAL. HAVELI,DIST. PUNE", "TAL-SHIRUR", "MAHALUNGE", "LONI KALBHORE", "PUNE SOLAPUR HIGHWAY", "RADHIKA ROAD", " PUNE SOLAPUR HIGHWAY", "CHINCHWAD", "KELAWADE", "BANER ", "BUND GARDEN ROAD", "MANJRI BUDRUK,DIST PUNE", "BHUINJ; DIST SATARA", "MARKETYARD KARAD", "NAVI PETH", "TAL SHIRUR, DIST PUNE", "KANHEPHATA", "TAL : PURANDAR", "Tal : Purandher", "SHIKRAPUR-RANJANGAO ROAD", "TALUKA PURANDAR", "PUNE TALUKA BHOR", " PISOLI ROAD", "NANAN PETH", "PUNE AHMAD NAGAR HIGHWAY", "TALUKA PURANDAR,DIST PUNE", "MIDC AIRPORT ROAD,", "VARKUTE BHDURUK", "Pune Ahmendnagar Road", "PUNE SOLAPUR HIGHWAY", "SATARA", "TAL.INDAPUR", "Kothrud", "PUNE NAGAR ROAD", "NAVI PETH", "TAL SHIRUR, DIST PUNE", "VARKUTE BHDURUK", "Pune Nashik Highway Tal Junner ", "BHIGWAN", "PUNE SOLAPUR HIGHWAY", "TALUK HAVELI", " Indapur-Baramati Rd", "Yawat", "NH4 ", "NEAR TALEGAON TOLL NAKA ", "WARJE", "KATRAJ", "NR NDA KHADAKWASLA", "SH60", "Ghorpuri, ", "DR.AMBEDKAR ROAD", "MIDC AIRPORT ROAD", "Baramati Phaltan Road", "PUNE PANDHARPUR ROAD", "Tal : Purandher", "TAL : PURANDAR", "PUNE - SHOLAPUR ROAD, TAL - HAVELI", "PHALTAN - BARAMATI ROAD", "PUNE BANGALORE HW", "PHALTAN-PANDHARPUR RD SATARA ", "NH-4, SATARA", "TAL KOREGAON, DIST SATARA ", "LAXMI NAGAR", "TQ PHALTAN, DIST SATARA", "AMBEGAON BR", "BARAMATI PATAS ROAD", "SHIRWAL", "WAKDEWADI", "PUNE BANGLORE HIGHWAY", "SATARA PANDHARPUR HIGHWAY", "MAHABALESHWAR MAIN ROAD", "PENCIL CHOWK", "PANDHARE", "ON SATARA LONAND ROAD, ", "PUNE BANGLORE HIGHWAY ", "SATARA KARAD ROAD", "BARAMATI PATAS ROAD, ", "WAKDEWADI", "WANOWRIE", "LAXMINAGAR", "KARAD", "KONDHAWA - PISOLI ROAD", "BIBVEWADI - KONDWA ROAD ", "TAL : PURANDHER", "BANER", "PCMC", "PCMC", "HAVELI ", "NH4 ", "SH 74, MASHWAD", "SATARA ", "PUNE-NAGAR HIGHWAY", "PUNE- BANGALORE HIGHWAY", "PHALTAN, SATARA", "PCMC TAL.HAVELI ", "Pune -Nashik Highway ", "PUNE SASWAD ROAD, BHEKRAINAGAR, FURSUNGI,", "BADHE CHOWK", "NH-4", "KELAWADE", "DHANKAWADI", "SHANKARSHET ROAD", "WARJE", "Taluka Mulshi", "PUNE AHMAD NAGAR HIGHWAY", "KATRAJ", "SHANKARSHET ROAD", "ON PUNE NAGAR ROAD", "ON PUNE NAGAR ROAD", "Mantakwadi", "LOHEGAON", "RAJGURUNAGAR", "DHANKAWADI", "TAL SATARA", "Tal : Khatav, Dist: Satara", "DIST SATARA ", "HADAPSAR", "PUNE SOLAPUR ROAD", "BHUKUM", "TAL HAVELI ", "TAL HAVELI ", "SATARA ", "NH-4", "DAUND", "SINHGAD ROAD", "NH50 ", "Alegaphata - Ahmednagar Road", "TAL. HAVELI,DIST", "AMBEGAON BUDRUK ", "NH50 ", "NH-4", "KARAD", "PCMC", "KASARWADI-", "NH-4"
    };
    public static String[] pumpCity = new String[]{"MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","THANE","MUMBAI","THANE","THANE","MUMBAI","NAVI-MUMBAI","MUMBAI","NAVI-MUMBAI","THANE","MUMBAI","THANE","MUMBAI","THANE","MUMBAI","THANE","MUMBAI","THANE","THANE","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","MUMBAI","THANE","THANE","MUMBAI","THANE","MUMBAI","THANE","THANE","MUMBAI",
            "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "TARADGAON", "SATARA", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "SATARA", "PUNE", "PUNE", "PUNE", "PUNE", "PUNAWALE - TAL. MULSHI", "PUNE", "TASVDE", "VAKHARI", "PUNE", "DAPODI", "VAKHARI", "DAPODI", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "VALSE", "VALSE", "PUNE", "PUNE", "PUNAWALE - TAL. MULSHI", "PUNE", "PUNE", "PUNE", "PUNE", "KAHNE", "PUNE", "SATARA", "MAHALUNGE, TAL - KHED", "LONIKAND", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "TAL BHOR, DIST PUNE", "BHUKUM", "MALEGAON", "PUNE", "PUNE", "PUNE", "PUNE", "MUNDHWA", "SATARA", "LONI KALBHORE", "BHUINJ", "PUNE", "LONIKAND", "SHIKRAPUR", "MAHALUNGE, TAL - KHED", "LONI KALBHORE", "PUNE", "SATARA", "MANJRI BUDRUK", "PUNE", "KELAWADE", "PUNE", "PUNE", "MANJRI BUDRUK", "BHUINJ", "KARAD", "PUNE", "PIMPALE JAGTAP", "KAHNE", "PUNE", "PUNE", "KONDHAPURI", "SASWAD", "NIGDE", "PISOLI", "PUNE", "LONI", "SASWAD", "BARAMATI", "INDAPUR", "PUNE", "MANJRI BUDRUK", "PUNE", "BHIGWAN", "PUNE", "KOREGAON BHIMA", "PUNE", "PIMPALE JAGTAP", "INDAPUR", "PUNE", "INDAPUR", "YAWAT", "PUNE", "INDAPUR", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "BARAMATI", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "PHALTAN", "SHIRWAL", "PIMPRAD", "KHORAD", "WATHAR", "LONAND", "ZIRAPWADI", "AMBEGAON", "BARAMATI", "SHINDEWADI", "PUNE", "SATARA", "SATARA", "SATARA", "BARAMATI", "PUNE", "AMBAWADE", "SIRUR PHATA", "SATARA", "BARAMATI", "PUNE", "PUNE", "SATARA", "KARAD", "PISOLI", "PUNE", "PUNE", "PUNE", "PUNE", "PUNE", "KESNAND ", "LONAWALA", "MHASVAD", "PHALTAN", "SHIKRAPUR", "PUNE", "TARADGAON", "PUNE", "PUNE", "PUNE", "PUNE", "TAL BHOR, DIST PUNE", "KELAWADE", "PUNE", "PUNE", "PUNE", "PUNE", "LONI", "PUNE", "PUNE", "SANASWADI", "WARWAND", "PUNE", "PUNE", "PUNE", "PUNE", "KASHIL", "MAINI", "KHANDALA", "PUNE", "PUNE", "BHUKUM", "PUNE", "PUNE", "SATARA", "KHORAD", "MALEGAON", "PUNE", "CHAKAN", "ALEPHATA - TAL JUNNER", "LONIKAND", "PUNE", "CHAKAN", "WATHAR - TAL KARAD", "TASVDE", "PUNE", "PUNE", "WATHAR - TAL KARAD"
    };
    public static String[] pumpPinCode = new String[]{"400001","400002","400003","400004","400005","400005","400005","400007","400007","400007","400007","400009","400010","400010","400010","400010","400011","400012","400014","400014","400015","400015","400015","400015","400016","400016","400018","400019","400020","400020","400022","400025","400026","400026","400028","400028","400031","400031","400036","400038","400038","400039","400050","400050","400051","400057","400058","400058","400058","400059","400059","400060","400062","400062","400064","400067","400067","400070","400071","400071","400071","400071","400071","400072","400074","400078","400078","400078","400079","400083","400086","400092","400092","400093","400098","400102","400103","400601","400601","400607","400607","400612","400701","400705","400706","400706","400709","401104","401203","401203","401403","401404","401501","401602","401606","410206","410206","410206","410206","410208","410218","421101","421204","421301","421302","421302","421302","421312","421601",
            "411030", "411014", "411001", "411016", "411030", "411005", "412201", "415528", "415003", "411014", "411009", "411016", "411030", "411004", "411005", "411017", "415523", "412203", "411040", "411028", "412207", "411033", "412302", "415110", "411045", "411040", "411012", "411045", "411012", "411030", "411051", "412210", "411030", "410506", "410512", "415102", "415102", "411001", "411005", "411033", "411017", "411048", "412304", "410505", "410512", "411001", "415003", "410501", "412216", "411057", "411057", "411001", "411005", "411001", "411009", "412205", "411042", "413102", "411030", "411038", "411004", "411033", "411041", "415001", "412201", "415513", "411030", "412216", "412208", "410501", "412201", "412203", "415001", "412307", "411019", "412213", "411045", "411001", "412307", "415513", "415110", "411030", "412208", "410512", "412302", "412203", "411048", "412301", "412205", "411028", "411002", "412210", "412301", "413133", "413106", "412208", "412307", "415003", "413130", "411038", "412216", "411030", "412208", "413106", "412412", "413130", "412214", "412205", "413120", "412214", "412106", "410506", "411029", "411046", "411023", "412220", "411001", "411001", "413133", "413102", "412206", "412203", "412302", "412110", "415523", "412801", "415523", "415519", "415524", "415521", "415523", "411046", "413102", "412801", "411003", "415105", "412501", "412806", "413102", "413110", "415011", "415517", "415001", "413102", "411003", "411040", "415523", "415110", "411028", "411040", "412301", "411045", "411033", "411027", "412207", "410401", "415509", "415528", "412208", "412207", "415528", "411027", "410504", "412302", "411048", "412205", "412213", "411043", "411009", "411029", "411057", "412210", "411046", "411009", "412208", "412215", "412308", "411047", "422201", "411043", "415519", "415102", "412802", "411028", "411013", "411042", "412105", "412105", "415003", "415519", "413105", "411051", "410501", "412411", "412216", "411046", "410501", "415110", "415110", "411033", "411026", "415110"
    };
    public static String[] pumpPhone = new String[]{"0","0","0","0","0","2222163655","0","0","0","0","0","0","0","0","0","0","0","4126872","0","0","0","0","0","24102162","0","0","0","0","0","0","0","4222422","2224946427","0","0","0","0","0","0","0","2222711219","0","0","0","9821139566","0","0","0","0","8594065","8388167","0","0","0","5823186","56018576","0","0","0","5203400","7692338","0","0","28578989","25996712","0","0","0","0","0","0","0","0","9820012157","0","6317695","2260603100","2225472779","5343063","252268036","5340442","27870716","9322211399","7672685","27707155","2227722215","7542214","8455212","2502502666","0","0","0","2525272318","9821138193","0","7452500","0","0","0","56165566","7420168","0","9820417923","911319355","0","9820012157","0","0","0",
            "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0", "0"
    };



    //get color
    public static int getColor(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 23) {
            return ContextCompat.getColor(context, id);
        } else {
            return context.getResources().getColor(id);
        }
    }


    //generate verification code
    public static int generateCode(){
        Random random = new Random();
        int rndno = (1000 + random.nextInt(9000));
        Log.d("myapp","code:"+rndno);
        return rndno;
    }

    // code validation
    public static boolean isCodeValid(Context context, int code){
        SharedPreferences preferences = context.getSharedPreferences(Const.SHAREDPREFERENCE_MAIN, Context.MODE_PRIVATE);
        int storedCode = preferences.getInt(Const.VERIFICATION_CODE,0);

        if(storedCode > 0 && code == storedCode){
            return true;
        }
        return false;
    }

//    // create directory in background
//    public static File createDirectoryInPhoneStorage(Context context){
//        File mkmydir = context.getDir(Const.DIR_NAME,Context.MODE_PRIVATE);
////        Log.d(Const.TAG,"dir path: "+mkmydir.getAbsolutePath());
//        if (!mkmydir.exists()){
//            mkmydir.mkdirs();
////            Log.d(Const.TAG,"new dir created for xml");
////            Log.d(Const.TAG,"1) dir length: "+mkmydir.listFiles().length);
//        }else{
////            Log.d(Const.TAG,"alredy folder exists");
////            Log.d(Const.TAG,"2) dir length: "+mkmydir.listFiles().length);
//        }
//        return mkmydir;
//    }

    //check if dir exists or not
    public static boolean isDirExists(){
        File xmlStorage = new File(Environment.getExternalStorageDirectory(),Const.DIR_NAME);
        if(xmlStorage.exists() && xmlStorage.isDirectory()){
            return true;
        }
        return false;
    }
    // create directory in background
    public static File createDirectory(Context context){
        File mediaStorageDir = new File(Environment.getExternalStorageDirectory(), Const.DIR_NAME);
//        Log.d(Const.TAG,"dir path: "+mkmydir.getAbsolutePath());
        if (!mediaStorageDir.exists()){
            mediaStorageDir.mkdirs();
            Log.d(Const.TAG,"new dir created for xml");
//            Log.d(Const.TAG,"1) dir length: "+mediaStorageDir.listFiles().length);
        }else{
            Log.d(Const.TAG,"alredy folder exists");
//            Log.d(Const.TAG,"2) dir length: "+mediaStorageDir.listFiles().length);
        }
        SharedPreferences.Editor editor = (context.getSharedPreferences(Const.SHAREDPREFERENCE_MAIN, Context.MODE_PRIVATE)).edit();
        editor.putString(Const.DIR_PATH,mediaStorageDir.getAbsolutePath());
        editor.apply();
        return mediaStorageDir;
    }


    public static void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }

        fileOrDirectory.delete();
        if(fileOrDirectory.exists()){
            Log.d(Const.TAG,"file deleted failed");
//            Log.d(Const.TAG,"After delete file count :" +fileOrDirectory.listFiles().length);
        }else{
            Log.d(Const.TAG,"file deleted conformed");
            //Log.d(Const.TAG,"After delete file count :" +fileOrDirectory.listFiles().length);
        }
    }


    public static void setOnetimeTimer(Context context) {
        AlarmManager am=(AlarmManager)context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, PushOnReceiver.class);
        intent.setAction("com.vnurture.vihas.smartcabfuelcard.ALARM_CALL");
//        intent.putExtra(Const.ONE_TIME, Boolean.TRUE);
        PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (1000 * 60 * 2), pi);
    }


    // All messages read
    public static Cursor readSMS (Context context){
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(SMS_INBOX, null,null, null, null);
        if(cursor != null){
            return cursor;
        }
        return null;
    }

    // specific date message read
    public static Cursor readSMS(Context context, String readDate, String readTime){
        Log.d(Const.TAG,"readSMS: readDate-"+readDate);
        // Now create a SimpleDateFormat object.
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'hh:mm:ss");
        String selectedDate = readDate+"T"+readTime;
        //For example: selectedDate="09-10-2015"+"T"+"00:00:00";
        // Now create a start time for this date in order to setup the filter.
        Date dateStart = null;
        try {
            dateStart = formatter.parse(selectedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Now create the filter and query the messages.
        String filter = "date >=" + dateStart.getTime();
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(SMS_INBOX, null, filter, null, null);
        if(cursor != null){
            return cursor;
        }
        return null;
    }

    // specific date message read
    public static Cursor readSMSBetweenDates(Context context, String readStartDate, String readStartTime, String readEndDate, String readEndTime){
        Log.d(Const.TAG,"readSMSBetweenDates: readStartDate-"+readStartDate+" readendDate: "+readEndDate);
        // Now create a SimpleDateFormat object.
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy'T'hh:mm:ss");
        String selectedStartDate = readStartDate+"T"+readStartTime;
        // for end date
        SimpleDateFormat formatter1 = new SimpleDateFormat("dd-MM-yyyy'T'hh:mm:ss");
        String selectedEndDate = readStartDate+"T"+readEndTime;
        //For example: selectedDate="09-10-2015"+"T"+"00:00:00";
        // Now create a start time for this date in order to setup the filter.
        Date dateStart = null;
        Date dateEnd = null;
        try {
            dateStart = formatter.parse(selectedStartDate);
            dateEnd = formatter1.parse(selectedEndDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // Now create the filter and query the messages.
        String filter = "date >=" + dateStart.getTime()+" and date <="+dateEnd.getTime();
        final Uri SMS_INBOX = Uri.parse("content://sms/inbox");
        Cursor cursor = context.getContentResolver().query(SMS_INBOX, null, filter, null, null);
        if(cursor != null){
            return cursor;
        }
        return null;
    }

    // get yesterday date
    public static String getYesterDayDate(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -1);
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(calendar.getTime());
    }

    // get current date
    public static String getCurrentDate(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        return df.format(calendar.getTime());
    }
    // increment Date
    public static String incrementDate(String currentDate){
        Log.d(Const.TAG,"New current DATE:"+currentDate);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(currentDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1);  // number of days to add
        String newDate = sdf.format(c.getTime());
        Log.d(Const.TAG,"New INCREMENTED DATE:"+newDate);
        return newDate;
    }


    public static boolean isDateMatched(String emailSendDate){
        Log.d(Const.TAG,"Utils : Dates matching...");
        String currentDate = getCurrentDate();
        if(currentDate.equals(emailSendDate)){
            Log.d(Const.TAG," date matched");
            return true;
        }else{
            Log.d(Const.TAG,"date not matched");
        }
        return false;
    }

    public static boolean isContainAnyTypeOfCode(String message){
        String tempMessage = message.replaceAll("\\s+","");
        String subtempMessage = tempMessage.substring(0,2);
        if(tempMessage.contains("OTP") || subtempMessage.contains("OTP") || tempMessage.contains("VerificationCode") || tempMessage.contains("OneTimePassword") || tempMessage.contains("verificationcode")){
            Log.d(Const.TAG,"matched String : "+tempMessage);
            return true;
        }
        return false;
    }


    public static boolean setAlarm(Context context) {

        Calendar calendarNow = Calendar.getInstance();
        Calendar calendarSet = Calendar.getInstance();

        calendarSet.set(Calendar.HOUR_OF_DAY, Const.ALARM_SET_HOUR); // hour
        calendarSet.set(Calendar.MINUTE, Const.ALARM_SET_MINUTE); // minute
        calendarSet.set(Calendar.SECOND, 0); // second

        if(calendarSet.compareTo(calendarNow) <= 0 ){
            calendarSet.set(Calendar.DAY_OF_WEEK,1);
            String message = "setAlarm: Time passed. New Alarm is set for tomorrow";
            makeLogAndEntry(message);
        }else{
            String message = "setAlarm: New Alarm Set";
            makeLogAndEntry(message);
        }
        //Intent intent = new Intent(context, SmartFuelAlarmReceiver.class);
        //PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1321, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        //AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        //alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendarSet.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);


        //ComponentName receiver = new ComponentName(context, SmartFuelAlarmReceiver.class);
        //PackageManager pm = context.getPackageManager();

//        pm.setComponentEnabledSetting(receiver,
//                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
//                PackageManager.DONT_KILL_APP);
        return true;
    }


    public static void makeLogAndEntry(String message){
        Log.d("myapp",message);
    }

}
