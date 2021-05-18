package com.miccha.server;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.*;

public class MYSQLConnectionTest {
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String URL = "JDBC:MYSQL://localhost:44444/miccha?serverTimezone=UTC&characterEncoding=UTF-8";
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
    private void insert(Connection con) {
        final String SQL= "INSERT INTO movie(id,title,description,rating,duration,thumbnail) VALUES (?,?,?,?,?,?)";

        try(PreparedStatement pstml = (PreparedStatement) con.prepareStatement(SQL)) {

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

            String result = sb.toString();

            JSONParser jsonParse = new JSONParser();
            JSONArray jsonArray = (JSONArray) jsonParse.parse(result);

            for(int i=0;i<jsonArray.size();i++){
                JSONObject obj=(JSONObject) jsonArray.get(i);
                if("Fantasy".equals(obj.get("theme"))) {
                    JSONArray moviesArray = (JSONArray) obj.get("movies");
                    for (int j = 0; j < moviesArray.size(); j++) {
                        JSONObject mobj = (JSONObject) moviesArray.get(j);

                        String id = (String) mobj.get("id");
                        String title = (String) mobj.get("title");
                        String description = (String) mobj.get("description");
                        String rating = (String) mobj.get("rating");
                        int duration = Integer.parseInt(String.valueOf(mobj.get("duration")));
                        String thumbnail = (String) mobj.get("thumbnail");

                        System.out.println(obj.get("theme") + ">>" + mobj);


                        pstml.setString(1, id);
                        pstml.setString(2, title);
                        pstml.setString(3, description);
                        pstml.setString(4, rating);
                        pstml.setInt(5, duration);
                        pstml.setString(6, thumbnail);

                        pstml.executeUpdate();
                    }
                }
            }
            pstml.close();
            con.close();
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("테이블에 행 삽입 실패");
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
