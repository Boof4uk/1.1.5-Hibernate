package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.HibernateConnection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import javax.persistence.Query;
import java.util.ArrayList;
import java.util.List;

import static jm.task.core.jdbc.util.HibernateConnection.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {
    public UserDaoHibernateImpl() {
    }

    @Override
    public void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users " +
                "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                "name VARCHAR(50) NOT NULL, " +
                "lastName VARCHAR(50) NOT NULL, " +
                "age TINYINT NOT NULL)";

        try (Session session = HibernateConnection.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.createNativeQuery(sql).executeUpdate();
            System.out.println("Таблица успешно создана");
            transaction.commit();
        } catch (Throwable e) {
            System.out.println("Не удалось создать таблицу");
        }
    }

    @Override
    public void dropUsersTable() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateConnection.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createSQLQuery("DROP TABLE IF EXISTS users").executeUpdate();
            System.out.println("Таблица успешно удалена");
            transaction.commit();
            session.close();
        } catch (Throwable e) {
            System.out.println("Не удалось удалить таблицу");
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try(Session session = HibernateConnection.getSessionFactory().openSession();) {
            Transaction transaction = session.beginTransaction();
            session.save(new User(name, lastName, age));
            System.out.println("User с именем – " + name + " добавлен в базу данных");
            transaction.commit();
        } catch (Throwable e) {
            System.out.println("Не удалось сохранить пользователя");
        }
    }

    @Override
    public void removeUserById(long id) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateConnection.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            User user = session.get(User.class, id);
            session.delete(user);
            transaction.commit();
            session.close();
            System.out.println("Пользователь с id " + id + " успешно удален");
        } catch (Throwable throwable) {
            System.out.println("Не удалось удалить пользователя");
        }

    }

    @Override
    public List<User> getAllUsers() {
        List<User> usersList = new ArrayList<>();
        try (Session session = HibernateConnection.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            usersList = session.createQuery("FROM User", User.class).getResultList();
            transaction.commit();
//            System.out.println("Лист пользователей: ");
        } catch (Throwable throwable) {
            System.out.println("Не удалось получить таблицу пользователей");
        }
        return usersList;
    }


    @Override
    public void cleanUsersTable() {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateConnection.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.createQuery("DELETE FROM User").executeUpdate();
            transaction.commit();
            session.close();
            System.out.println("Таблица очищена");
        } catch (Throwable throwable) {
            System.out.println("Не удалось очистить таблицу");
        }

    }
}
