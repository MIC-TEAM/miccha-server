package com.miccha.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class MYSQLConnectionTest {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "JDBC:MYSQL://router.debiandebian.com:44444/miccha?serverTimezone=UTC&characterEncoding=UTF-8";
    private static final String USER = "dhlee";
    private static final String PW = "test1234";

    @Test
    public void testConnection() throws Exception{
        Class.forName(DRIVER);

        try(Connection con = DriverManager.getConnection(URL,USER,PW)){
            //System.out.println(con);
            insert(con);//데이터 입력
        }catch(Exception e) {
            e.printStackTrace();
        }
    }

    //삽입
    private void insert(Connection con) throws IOException, ParseException, SQLException {

        String result=null;
        try{
            String apiUrl = "https://router.debiandebian.com/v1/movies";
            URL url = new URL(apiUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");
            BufferedReader rd;
            if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            result = sb.toString();
        }catch (Exception e) {
            throw e;
        }

        JSONParser jsonParse = new JSONParser();
        JSONArray jsonArray = (JSONArray) jsonParse.parse(result);

        for(int i=0;i<jsonArray.size();i++){
            JSONObject obj=(JSONObject) jsonArray.get(i);
            JSONArray moviesArray = (JSONArray) obj.get("movies");
            if("Sci-Fi".equals(obj.get("theme"))) {
                for (int j = 0; j < moviesArray.size(); j++) {
                    JSONObject mobj = (JSONObject) moviesArray.get(j);

                    String title = (String) mobj.get("title");
                    String description = (String) mobj.get("description");
                    String rating = (String) mobj.get("rating");
                    int duration = ((Long) mobj.get("duration")).intValue();;
                    String thumbnail = (String) mobj.get("thumbnail");
                    try{
                        String query="INSERT INTO movie(title,description,rating,duration,thumbnail) VALUES (?,?,?,?,?)";
                        PreparedStatement pstmt = con.prepareStatement(query);

                        pstmt.setString(1, title);
                        pstmt.setString(2, description);
                        pstmt.setString(3, rating);
                        pstmt.setInt(4, duration);
                        pstmt.setString(5, thumbnail);

                        pstmt.executeUpdate();
                        pstmt.close();
                        System.out.println(obj.get("theme") + ">>" + mobj);
                    } catch (Exception e) {
                        throw e;
                    }
                }
                con.close();
            }
        }
    }

    public void select(Connection con) throws Exception {
        try{
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from movie");
            while (rs.next()){
                System.out.println(">>>> id:"+rs.getString("id")+", val:"+rs.getString("title"));
            }
            rs.close();
            stmt.close();
            con.close();
        } catch (Exception e) {
            throw e;
        }
    }

    public void delete(Connection con) throws Exception {
        try{
            PreparedStatement pstmt = con.prepareStatement("delete from movie");
            pstmt.executeUpdate();

            pstmt.close();
            con.close();
        } catch (Exception e) {
            throw e;
        }
    }
}
