package com.sochoeun.service;

import com.sochoeun.model.Team;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TeamService {
    Team createTeam(Team request);
    List<Team> getTeamList();
    Team getTeam(Integer teamId);
    Team updateTeam(Integer teamId,Team request);
    void deleteTeam(Integer teamId);

    String uploadPhoto(Integer teamId, MultipartFile file);

}
