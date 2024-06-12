package com.sochoeun.service.impl;

import com.sochoeun.exception.NotFoundException;
import com.sochoeun.model.Team;
import com.sochoeun.repository.TeamRepository;
import com.sochoeun.service.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamServiceImpl implements TeamService {
    private final TeamRepository teamRepository;

    @Value("${application.upload.server.path}"+"/team/")
    String serverPath;
    @Override
    public Team createTeam(Team request) {
        return teamRepository.save(request);
    }

    @Override
    public List<Team> getTeamList() {
        return teamRepository.findAll();
    }

    @Override
    public Team getTeam(Integer teamId) {
        return teamRepository.findById(teamId).orElseThrow(()->new NotFoundException("Team",teamId));
    }

    @Override
    public Team updateTeam(Integer teamId, Team request) {
        Team team = getTeam(teamId);
        team.setName(request.getName());
        team.setBio(request.getBio());
        team.setDescription(request.getDescription());
        return teamRepository.save(team);
    }

    @Override
    public void deleteTeam(Integer teamId) {
        getTeam(teamId);
        teamRepository.deleteById(teamId);
    }

    @Override
    public String uploadPhoto(Integer teamId, MultipartFile file) {
        String photoName = String.valueOf(Calendar.getInstance().getTimeInMillis());
        String photoUrl  =photoFunction.apply(photoName,file);
        Team team = getTeam(teamId);
        team.setPhotoUrl(photoUrl);
        teamRepository.save(team);
        return photoUrl;
    }

    private final Function<String,String> fileExtension =
            filename -> Optional.of(filename)
                    .filter(name -> name.contains("."))
                    .map(name
                            -> "." + name.substring(filename.lastIndexOf(".")+1)).orElse(".png");

    private final BiFunction<String,MultipartFile,String> photoFunction = (id, image) ->{
        try{
            Path fileStorageLocation = Paths.get(serverPath).toAbsolutePath().normalize();
            if (!Files.exists(fileStorageLocation)){
                Files.createDirectories(fileStorageLocation);
            }

            Files.copy(
                    image.getInputStream(),
                    fileStorageLocation.resolve(id + fileExtension.apply(image.getOriginalFilename())), // filename
                    REPLACE_EXISTING);

            return ServletUriComponentsBuilder
                    .fromCurrentContextPath() // localhost:8080
                    .path("/api/teams/image/" + id + fileExtension.apply(image.getOriginalFilename())).toUriString();
        }catch (Exception e){
            throw new RuntimeException("Unable to save image");
        }
    };
}