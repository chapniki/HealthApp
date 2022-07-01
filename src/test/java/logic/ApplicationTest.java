package logic;

import cz.vse.fimed.dbapi.JDBCDao;
import cz.vse.fimed.profile.Doctor;
import cz.vse.fimed.profile.Pacient;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class ApplicationTest {
    //instance pacienta existujícího v db
    Pacient jirko = new Pacient("duqboqfb@qwd.wqd", "wfwfpwfmf", "+420798798798", "Dar", "Malik", true, 78, "M", new ArrayList<>(), 100, 190, "Jankova 2326/60, Praha");
    //instance pacienta který v db není
    Pacient kara = new Pacient("dsdosodo@dq.com", "dar@cppwppwcom", "+420asdassdas", "Dar", "Malik", true, 78, "M", new ArrayList<>(), 100, 190, "Jankova 2326/60, Praha");

    //instance lékaře existujícího v db
    Doctor pupa = new Doctor("okjgege@dqd.if", "dsffwffffsf", null, null, null, false, 0, null, null, null, null, null, 0);
    //instance lékaře který v db není
    Doctor lupa  = new Doctor("pqdqbuf@qfqf.fd", "add9f99fgdosd", null, null, null, false, 0, null, null, null, null, null, 0);



    //testovani registrace uživatele, ověření vstupu
    @Test
    public void checkUserLoginDataTest() throws SQLException {

        //registrace
        JDBCDao.registerUser(jirko, null);
        JDBCDao.registerUser(null, pupa);

        //ověření zda se uživatele zaregistrovali a zda metoda ověřující vstup funguje správně
        assertNotEquals(null, JDBCDao.checkUserLoginData(jirko.getEmail(), jirko.getPassword()));
        assertEquals(null, JDBCDao.checkUserLoginData(kara.getEmail(), kara.getPassword()));

        assertNotEquals(null, JDBCDao.checkUserLoginData(pupa.getEmail(), pupa.getPassword()));
        assertEquals(null, JDBCDao.checkUserLoginData(lupa.getEmail(), lupa.getPassword()));

        //testovani getuserinfo
        assertEquals(jirko.getEmail(), JDBCDao.getUserInfo(JDBCDao.getUserId(jirko,null)).getEmail());

        //testovani zmeny hesla
        JDBCDao.changePassword(JDBCDao.getUserId(jirko,null), "floppafloppa");
        assertNotEquals(jirko.getPassword(), JDBCDao.getUserInfo(JDBCDao.getUserId(jirko,null)).getPassword());

        //pokus poslat zpravu lekari
        assertEquals(false,JDBCDao.checkAppointment(pupa.getEmail()));
        JDBCDao.makeAppointment(pupa.getEmail());
        assertEquals(true,JDBCDao.checkAppointment(pupa.getEmail()));

        //testovani metod pro manipulaci se symptomy
        assertTrue(JDBCDao.getSymthoms(JDBCDao.getUserId(jirko,null)).isEmpty());
        JDBCDao.addSymthom("ddddddddd");
        assertNotNull(JDBCDao.getSymthoms(JDBCDao.getUserId(jirko,null)));
        JDBCDao.clearSymps();
        assertTrue(JDBCDao.getSymthoms(JDBCDao.getUserId(jirko,null)).isEmpty());


        //testujeme metody ktere vraceji instance vsech lekaru a pacientu
        boolean k = false;
        for (Doctor i : JDBCDao.getDoctors()){if(i.getEmail().equals(pupa.getEmail())) k = true;}
        assertTrue(k);

        boolean p = false;
        for (Pacient i : JDBCDao.getPatients()){if(i.getEmail().equals(jirko.getEmail())) p = true;}
        assertTrue(p);


        //odstraneni uzivatelu, po odstraneni se nevyhledavaji instance
        JDBCDao.deleteUser(JDBCDao.getUserId(jirko,null));
        JDBCDao.deleteUser(JDBCDao.getUserId(null,pupa));


        boolean pk = false;
        for (Pacient i : JDBCDao.getPatients()){if(i.getEmail().equals(jirko.getEmail())) pk = true;}
        assertFalse(pk);

        boolean kp = false;
        for (Doctor i : JDBCDao.getDoctors()){if(i.getEmail().equals(pupa.getEmail())) kp = true;}
        assertFalse(kp);


    }

}


