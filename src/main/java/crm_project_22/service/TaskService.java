package crm_project_22.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import crm_project_22.entity.Task;
import crm_project_22.repository.TaskRepository;

public class TaskService {
    private TaskRepository taskRepository = new TaskRepository();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd HH:mm:ss

    public boolean create(HttpServletRequest req, HttpServletResponse resp) {
        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        int idUser = Integer.parseInt(req.getParameter("idUser"));
        int idProject = Integer.parseInt(req.getParameter("idProject"));
        int idStatus = Integer.parseInt(req.getParameter("idStatus"));

        Timestamp parsedStartDate = null;
        Timestamp parsedEndDate = null;

        if (!startDate.isBlank() && !endDate.isBlank()) {
            try {
                parsedStartDate = new Timestamp(dateFormat.parse(startDate).getTime());
                parsedEndDate = new Timestamp(dateFormat.parse(endDate).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return taskRepository.create(name, description, parsedStartDate, parsedEndDate, idUser,
                idProject, idStatus);
    }

    public List<Task> getAll() {
        return taskRepository.getAll();
    }

    public List<Task> getAllOfOneUser(int userId) {
        return taskRepository.getAllOfOneUser(userId);
    }

    public List<Task> getWithNoUserInProject(int projectId) {
        return taskRepository.getWithNoUserInProject(projectId);
    }

    public Task getById(int id) {
        return taskRepository.getById(id);
    }

    public boolean updateById(HttpServletRequest req, HttpServletResponse resp)
            throws JsonSyntaxException, JsonIOException, IOException {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        int id = Integer.parseInt(req.getParameter("id"));

        String name = data.get("name").getAsString();
        String description = data.get("description").getAsString();
        String startDate = data.get("startDate").getAsString();
        String endDate = data.get("endDate").getAsString();
        int oldIdUser = data.get("oldIdUser").getAsInt();
        int idUser = data.get("idUser").getAsInt();
        int idStatus = data.get("idStatus").getAsInt();

        Timestamp parsedStartDate = null;
        Timestamp parsedEndDate = null;

        if (!startDate.isBlank() && !endDate.isBlank()) {
            try {
                parsedStartDate = new Timestamp(dateFormat.parse(startDate).getTime());
                parsedEndDate = new Timestamp(dateFormat.parse(endDate).getTime());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        return taskRepository.updateById(id, name, description, parsedStartDate, parsedEndDate,
                oldIdUser, idUser, idStatus);
    }

    public boolean updateStatus(int id, int idStatus) {
        return taskRepository.updateStatus(id, idStatus);
    }

    public boolean deleteById(int id) {
        return taskRepository.deleteById(id);
    }
}
