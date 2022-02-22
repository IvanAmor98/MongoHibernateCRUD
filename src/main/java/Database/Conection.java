package Database;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

public class Conection {
	private static SessionFactory instance;
	private static MongoClient client;
	private static MongoDatabase database;

	public static SessionFactory getConnection() {
		if (instance == null) {
			instance = (SessionFactory) new Configuration().configure()
					.buildSessionFactory(new StandardServiceRegistryBuilder().configure().build());
			return instance;
		} else {
			return instance;
		}
	}

	public static void closeConnection() {
		instance.close();
		((MongoClient) database).close();
	}

	public static MongoDatabase getMongoConnection() {
		if (database == null) {
			client = MongoClients.create();
			database = client.getDatabase("empresa");
		}
		return database;
	}

}
