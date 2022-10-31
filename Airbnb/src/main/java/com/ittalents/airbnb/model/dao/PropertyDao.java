package com.ittalents.airbnb.model.dao;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.ittalents.airbnb.model.entity.Property;
import com.ittalents.airbnb.model.exceptions.NotFoundException;
import com.ittalents.airbnb.model.repositories.PropertyRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class PropertyDao {
    @Autowired
    ModelMapper modelMapper;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    PropertyRepository propertyRepository;
    public final int MIN = 0;
    public final int MAX = 1000;
    public final String FIND_BY_COUNTRY_CITY_AND_EXTRAS="SELECT p.id \n" +
            "FROM properties as p JOIN addresses as a ON(a.a_id = p.id) \n" +
            "WHERE ((a.city = ? AND a.country = ?  )AND (p.extras & ?)=? AND (p.max_guests  BETWEEN ? AND ? ) AND (p.beds  BETWEEN ? AND ? ) AND (p.bathrooms BETWEEN ? AND ? ) )   LIMIT ?,? ;";
    //AND (p.extras & ?)=?    LIMIT ?,?
    public final String FIND_BY_COUNTRY_AND_EXTRAS="SELECT p.id \n" +
            "FROM properties as p JOIN addresses as a ON(a.a_id = p.id) \n" +
            "WHERE ((a.country = ? AND (extras & ?)=? ) AND (p.max_guests  BETWEEN ? AND ? ) AND (p.beds   BETWEEN ? AND ? ) AND (p.bathrooms BETWEEN ? AND ? ))     LIMIT ?,? ;";
    public final String FIND_BY_CITY_AND_EXTRAS="SELECT p.id \n" +
            "FROM properties as p JOIN addresses as a ON(a.a_id = p.id) \n" +
            "WHERE ((a.city = ?  AND (extras & ?)=? ) AND (p.max_guests  BETWEEN ? AND ? ) AND (p.beds  BETWEEN ? AND ? ) AND (p.bathrooms  BETWEEN ? AND ? ))    LIMIT ?,?  ;";
    public final String FIND_BY_EXTRAS="SELECT p.id\n" +
            "FROM properties as p JOIN addresses as a ON(a.a_id = p.id) \n" +
            "WHERE( ( (extras & ?)=? ) AND (p.max_guests  BETWEEN ? AND ? ) AND (p.beds BETWEEN ? AND ? ) AND (p.bathrooms BETWEEN ? AND ? ))     LIMIT ?,?  ;";

    public List<Property> byCityCountryAndExtras(int offset, int pageSize,String city,String country,int extrasFilter,int maxGuests,int beds,int bathrooms){

       List<Property> properties = jdbcTemplate.query(FIND_BY_COUNTRY_CITY_AND_EXTRAS, new PreparedStatementSetter() {
           @Override
           public void setValues(PreparedStatement ps) throws SQLException {
               int maxGuestsLower ;
               int maxGuestsUpper ;
               int bedsLower;
               int bedsUpper;
               int bathroomsLower;
               int bathroomsUpper;
               if(maxGuests==0){
                   maxGuestsLower = MIN;
                   maxGuestsUpper = MAX;
               }
               else{
                   maxGuestsLower = maxGuests;
                   maxGuestsUpper = maxGuests;
               }
               if(beds == 0){
                   bedsLower = MIN;
                   bedsUpper = MAX;
               }
               else{
                   bedsLower = beds;
                   bedsUpper = beds;
               }
               if(bathrooms == 0){
                   bathroomsLower = MIN;
                   bathroomsUpper = MAX;
               }
               else{
                   bathroomsLower = bathrooms;
                   bathroomsUpper = bathrooms;
               }
               ps.setString(1,city);
               ps.setString(2,country);
               ps.setInt(3,extrasFilter);
               ps.setInt(4,extrasFilter);
               ps.setInt(5,maxGuestsLower);
               ps.setInt(6,maxGuestsUpper);
               ps.setInt(7,bedsLower);
               ps.setInt(8,bedsUpper);
               ps.setInt(9,bathroomsLower);
               ps.setInt(10,bathroomsUpper);
               ps.setInt(11,offset*pageSize);
               ps.setInt(12,pageSize);
           }
       }, new ResultSetExtractor<List<Property>>() {
           @Override
           public List<Property> extractData(ResultSet rs) throws SQLException, DataAccessException {
               List<Property> properties = new ArrayList<>();
               System.out.println(rs.getFetchSize());
               while (rs.next()) {
                   properties.add(propertyRepository.findById(rs.getLong(1)).get());

               }

               return properties;
           }
       });

         return properties;

    }
    public List<Property> byCountryAndExtras(int offset, int pageSize,String country,int extrasFilter,int maxGuests,int beds,int bathrooms){

        return getProperties(offset, pageSize, country, extrasFilter, FIND_BY_COUNTRY_AND_EXTRAS, maxGuests,beds,bathrooms);

    }

    private List<Property> getProperties(int offset, int pageSize, String country, int extrasFilter, String find_by_country_and_extras,int maxGuests,int beds,int bathrooms) {
        List<Property> properties = jdbcTemplate.query(find_by_country_and_extras, ps -> {

            int maxGuestsLower ;
            int maxGuestsUpper ;
            int bedsLower;
            int bedsUpper;
            int bathroomsLower;
            int bathroomsUpper;
            if(maxGuests==0){
                maxGuestsLower = MIN;
                maxGuestsUpper = MAX;
            }
            else{
                maxGuestsLower = maxGuests;
                maxGuestsUpper = maxGuests;
            }
            if(beds == 0){
                bedsLower = MIN;
                bedsUpper = MAX;
            }
            else{
                bedsLower = beds;
                bedsUpper = beds;
            }
            if(bathrooms == 0){
                bathroomsLower = MIN;
                bathroomsUpper = MAX;
            }
            else{
                bathroomsLower = bathrooms;
                bathroomsUpper = bathrooms;
            }

            ps.setString(1,country);
            ps.setInt(2,extrasFilter);
            ps.setInt(3,extrasFilter);
            ps.setInt(4,maxGuestsLower);
            ps.setInt(5,maxGuestsUpper);
            ps.setInt(6,bedsLower);
            ps.setInt(7,bedsUpper);
            ps.setInt(8,bathroomsLower);
            ps.setInt(9,bathroomsUpper);
            ps.setInt(10,offset*pageSize);
            ps.setInt(11,pageSize);
        }, rs -> {
            List<Property> properties1 = new ArrayList<>();
          //  System.out.println(rs.getFetchSize());
            while (rs.next()) {
                properties1.add(propertyRepository.findById(rs.getLong(1)).get());
            //    System.out.println("OP");
            }

            return properties1;
        });

        return properties;
    }

    public List<Property> byCityAndExtras(int offset, int pageSize,String city,int extrasFilter,int maxGuests,int beds,int bathrooms){

        return getProperties(offset, pageSize, city, extrasFilter, FIND_BY_CITY_AND_EXTRAS,maxGuests,beds,bathrooms);

    }

    public List<Property> byExtras(int offset, int pageSize,int extrasFilter,int maxGuests,int beds,int bathrooms){

        List<Property> properties = jdbcTemplate.query(FIND_BY_EXTRAS, new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
                int maxGuestsLower ;
                int maxGuestsUpper ;
                int bedsLower;
                int bedsUpper;
                int bathroomsLower;
                int bathroomsUpper;
                if(maxGuests==0){
                    maxGuestsLower = MIN;
                    maxGuestsUpper = MAX;
                }
                else{
                    maxGuestsLower = maxGuests;
                    maxGuestsUpper = maxGuests;
                }
                if(beds == 0){
                    bedsLower = MIN;
                    bedsUpper = MAX;
                }
                else{
                    bedsLower = beds;
                    bedsUpper = beds;
                }
                if(bathrooms == 0){
                    bathroomsLower = MIN;
                    bathroomsUpper = MAX;
                }
                else{
                    bathroomsLower = bathrooms;
                    bathroomsUpper = bathrooms;
                }

                ps.setInt(1,extrasFilter);
                ps.setInt(2,extrasFilter);
                ps.setInt(3,maxGuestsLower);
                ps.setInt(4,maxGuestsUpper);
                ps.setInt(5,bedsLower);
                ps.setInt(6,bedsUpper);
                ps.setInt(7,bathroomsLower);
                ps.setInt(8,bathroomsUpper);
                ps.setInt(9,offset);
                ps.setInt(10,pageSize);
            }
        }, rs -> {
            List<Property> properties1 = new ArrayList<>();
            while (rs.next()) {
                properties1.add(propertyRepository.findById(rs.getLong(1)).get());
            }

            return properties1;
        });

        return properties;

    }

}
