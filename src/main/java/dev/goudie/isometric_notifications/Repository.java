package dev.goudie.isometric_notifications;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@org.springframework.stereotype.Repository
public class Repository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public Repository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public String getUserSubscription(String userId) {
        String sql = "select u.\"pushNotificationSubscription\"  from isometric.\"User\" u where u.\"userId\" = :userId";

        MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource().addValue(
                "userId",
                userId
        );

        return jdbcTemplate.queryForObject(
                sql,
                mapSqlParameterSource,
                String.class
        );
    }
}
