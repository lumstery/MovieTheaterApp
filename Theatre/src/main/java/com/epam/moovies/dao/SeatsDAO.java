package com.epam.moovies.dao;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.stereotype.Repository;

import com.epam.moovies.model.Seat;

@Repository
public class SeatsDAO extends AbstractDAO<Seat> {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8435126481029638320L;

	@Override
	public Seat add(Seat item) {
		String query = "INSERT INTO seats (isVip,number,auditory) VALUES (?,?,?)";

		jdbcTemplate.update(connection -> {
			PreparedStatement preparedStatement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setBoolean(1, item.isVip());
			preparedStatement.setLong(2, item.getNumber());
			preparedStatement.setLong(3, item.getAuditoryId());
			return preparedStatement;
		});
		return item;
	}

	@Override
	public boolean update(Seat item) {
		String query = "UPDATE  seats SET isVip = ? WHERE number=? AND auditory=?   ";
		int updated = jdbcTemplate.update(connection -> {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setBoolean(1, item.isVip());
			preparedStatement.setLong(2, item.getNumber());
			preparedStatement.setLong(3, item.getAuditoryId());
			return preparedStatement;
		});
		return updated > 0;
	}

	@Override
	public boolean remove(Long key) {
		String query = "DELETE FROM seats  WHERE id=? ";
		int removed = jdbcTemplate.update(connection -> {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setLong(1, key);
			return preparedStatement;
		});
		return removed > 0;
	}

	@Override
	public List<Seat> getAll() {
		String query = "SELECT * FROM seats ";
		List<Seat> seatsList = jdbcTemplate.query(query, (resultSet, i) -> {
			return getSeatFromRS(resultSet);
		});
		return seatsList;
	}

	@Override
	public Seat getById(Long key) {
		String query = "SELECT * FROM seats WHERE id=?";
		return jdbcTemplate.queryForObject(query, (resultSet, i) -> {
			return getSeatFromRS(resultSet);
		}, key);
	}

	public List<Seat> getSeatsForTicket(long id) {
		String query = "SELECT * FROM seats s  JOIN  seats_tickets st ON s.number=st.seat_number AND s.auditory=st.auditory_id WHERE st.ticket_id=? ;";
		List<Seat> seatsList = jdbcTemplate.query(query, (resultSet, i) -> {
			return getSeatFromRS(resultSet);
		}, id);
		return seatsList;
	}

	private Seat getSeatFromRS(ResultSet resultSet) throws SQLException {
		Seat seat = new Seat();
		seat.setNumber(resultSet.getLong("number"));
		seat.setAuditoryId(resultSet.getLong("auditory"));
		seat.setVip(resultSet.getBoolean("isVip"));
		return seat;
	}

	public void assignSeatsWithTicket(List<Seat> bookedSeats, Long ticketId) {
		String query = "INSERT INTO seats_tickets (seat_number,auditory_id,ticket_id) VALUES (?,?,?)";
		jdbcTemplate.batchUpdate(query, new BatchPreparedStatementSetter() {
			@Override
			public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
				preparedStatement.setLong(1, bookedSeats.get(i).getNumber());
				preparedStatement.setLong(2, bookedSeats.get(i).getAuditoryId());
				preparedStatement.setLong(3, ticketId);
			}

			@Override
			public int getBatchSize() {
				return bookedSeats.size();
			}
		});

	}
}
