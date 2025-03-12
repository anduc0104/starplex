package com.cinema.starplex.ui.controllers.admin;

import com.cinema.starplex.dao.RoomDao;
import com.cinema.starplex.models.Room;

import java.util.List;

public class RoomController {
    private RoomDao roomDao = new RoomDao();

    public void addRoom(Room room) {
        roomDao.save(room);
        System.out.println("Room added successfully!");
    }

    public void updateRoom(Room room) {
        roomDao.update(room);
        System.out.println("Room updated successfully!");
    }

    public void deleteRoom(Room room) {
        roomDao.delete(room.getId().longValue());
        System.out.println("Room deleted successfully!");
    }

    public Room getRoomById(long id) {
        return roomDao.findById(id);
    }

    public List<Room> getAllRooms() {
        return roomDao.findAll();
    }
}
