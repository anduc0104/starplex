package com.cinema.starplex.dao;

import com.cinema.starplex.models.Room;
import com.cinema.starplex.models.Seat;


import java.util.List;

public class RoomDao implements BaseDao<Room> {
    @Override
    public void save(Room room) {

    }

    @Override
    public boolean insert(Room entity) {
        return false;
    }

    @Override
    public void update(Room room) {

    }

    @Override
    public void delete(long id) {

    }

    @Override
    public Room findById(long id) {
        return null;
    }

    @Override
    public List<Room> findAll() {
        return null;

    }

    public List<Room> findRoomByShowtimeId(long showtimeId) {
        return null;

    }

    public List<Seat> findSeatsByRoomId(long roomId) {
        return null;

    }
}
