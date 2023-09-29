package crm_project_22.service;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import crm_project_22.entity.Project;
import crm_project_22.entity.User;
import crm_project_22.repository.ProjectRepository;

public class ProjectService {
    private ProjectRepository projectRepository = new ProjectRepository();
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // yyyy-MM-dd HH:mm:ss

    public boolean create(HttpServletRequest req, HttpServletResponse resp) {
        HttpSession httpSession = req.getSession();
        User user = (User) httpSession.getAttribute("userLoggedIn");

        String name = req.getParameter("name");
        String description = req.getParameter("description");
        String startDate = req.getParameter("startDate");
        String endDate = req.getParameter("endDate");
        int idUser = user.getId();
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

        return projectRepository.create(name, description, parsedStartDate, parsedEndDate, idUser,
                idStatus);
    }

    public List<Project> getAll() {
        return projectRepository.getAll();
    }

    public List<Project> getAllOfOneLeader(int userId) {
        return projectRepository.getAllOfOneLeader(userId);
    }

    public Project getById(int id) {
        return projectRepository.getById(id);
    }

    public boolean updateById(HttpServletRequest req, HttpServletResponse resp)
            throws JsonSyntaxException, JsonIOException, IOException {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        int id = Integer.parseInt(req.getParameter("id"));

        String name = data.get("name").getAsString();
        String description = data.get("description").getAsString();
        String startDate = data.get("startDate").getAsString();
        String endDate = data.get("endDate").getAsString();
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

        return projectRepository.updateById(id, name, description, parsedStartDate, parsedEndDate,
                idStatus);

    }

    public boolean addMembers(HttpServletRequest req, HttpServletResponse resp)
            throws JsonSyntaxException, JsonIOException, IOException {
        JsonObject data = new Gson().fromJson(req.getReader(), JsonObject.class);
        int id = Integer.parseInt(req.getParameter("id"));

        JsonArray newMembers = data.get("newMembers").getAsJsonArray();

        int[] newMembersIds = new int[newMembers.size()];
        for (int i = 0; i < newMembers.size(); i++) {
            newMembersIds[i] = newMembers.get(i).getAsInt();
        }

        return projectRepository.createManyProjectUser(id, newMembersIds);
    }

    public boolean deleteById(int id) {
        return projectRepository.deleteById(id);
    }
}
