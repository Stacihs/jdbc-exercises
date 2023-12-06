package dao;

import com.mysql.cj.jdbc.Driver;
import config.Config;
import models.Album;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class MySQLAlbumsDAO {
    // initialize the connection to null so we know whether or not to close it when done
    private Connection connection = null;

    public void createConnection() throws MySQLAlbumsException {
        System.out.print("Trying to connect... ");
        try {
            DriverManager.registerDriver(new Driver());

            connection = DriverManager.getConnection(
                    Config.getUrl(),
                    Config.getUser(),
                    Config.getPassword()
            );

            System.out.println("connection created.");
        } catch (SQLException e) {
            throw new MySQLAlbumsException("connection failed!!!");
        }
    }

    int getTotalAlbums() throws MySQLAlbumsException {
        int count = 0;
        try {
            Statement statement = connection.createStatement();

            // Execute statement
            ResultSet resultSet = statement.executeQuery("SELECT * FROM albums");
            while (resultSet.next()) {
                count++;
            }
        } catch (SQLException e) {
            throw new MySQLAlbumsException("Error executing query: " + e.getMessage() + "!!!");
        }
        return count;
    }

    public void closeConnection() {
        if (connection == null) {
            System.out.println("Connection aborted.");
            return;
        }
        try {
            connection.close();
            System.out.println("Connection closed.");
        } catch (SQLException e) {
            // ignore this
        }
    }

    public List<Album> fetchAlbums() throws MySQLAlbumsException {
        List<Album> albums = new ArrayList<>();
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM albums");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                Album album = new Album();
                album.setId(resultSet.getInt("id"));
                album.setArtist(resultSet.getString("artist"));
                album.setName(resultSet.getString("name"));
                album.setReleaseDate(resultSet.getInt("release_date"));
                album.setSales(resultSet.getDouble("sales"));
                album.setGenre(resultSet.getString("genre"));
                albums.add(album);
            }
            return albums;
        } catch (SQLException e) {
            throw new MySQLAlbumsException("Something went wrong");
        }
    }


    public Album fetchAlbumById(long id) {
        Album album;
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM albums WHERE id = ?");
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();

            album = new Album();
            album.setId(resultSet.getLong("id"));
            album.setArtist(resultSet.getString("artist"));
            album.setName(resultSet.getString("name"));
            album.setReleaseDate(resultSet.getInt("release_date"));
            album.setSales(resultSet.getDouble("sales"));
            album.setGenre(resultSet.getString("genre"));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return album;
    }


    // Note that insertAlbum should return the id that MySQL creates for the new inserted album record
    public long insertAlbum(Album album) throws MySQLAlbumsException {
        long id = 0;
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO albums" +
                    " (artist, name, release_date, sales, genre)" +
                    "VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
            statement.setString(1, album.getArtist());
            statement.setString(2, album.getName());
            statement.setInt(3, album.getReleaseDate());
            statement.setDouble(4, album.getSales());
            statement.setString(5, album.getGenre());

            statement.executeUpdate();

            ResultSet keys = statement.getGeneratedKeys();
            keys.next();

            return keys.getLong(1);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    public void updateAlbum(Album album) throws MySQLAlbumsException {

        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE albums" +
                    " SET artist = ? " +
                    ", name = ? " +
                    ", release_date = ?" +
                    ", sales = ?" +
                    ", genre = ?" +
                    " WHERE id = ?");

            statement.setString(1, album.getArtist());
            statement.setString(2, album.getName());
            statement.setInt(3, album.getReleaseDate());
            statement.setDouble(4, album.getSales());
            statement.setString(5, album.getGenre());
            statement.setLong(6, album.getId());
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void deleteAlbumById(long id) throws MySQLAlbumsException {
        try {
            PreparedStatement statement = connection.prepareStatement("DELETE FROM albums WHERE id = ?");
            statement.setLong(1, id);

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new MySQLAlbumsException(e.getMessage());
        }
    }

}
