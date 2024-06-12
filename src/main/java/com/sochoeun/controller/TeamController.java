package com.sochoeun.controller;

import com.sochoeun.model.BaseResponse;
import com.sochoeun.model.Team;
import com.sochoeun.service.TeamService;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.util.MimeTypeUtils.IMAGE_JPEG_VALUE;
import static org.springframework.util.MimeTypeUtils.IMAGE_PNG_VALUE;

@RestController
@RequestMapping("/api/teams")
@RequiredArgsConstructor
public class TeamController {
    private final TeamService teamService;
    @Value("${application.upload.server.path}"+"/team/")
    String serverPath;
    private BaseResponse baseResponse;

    @PostMapping
    public ResponseEntity<?> createTeam(@RequestBody Team request){
        Team team = teamService.createTeam(request);
        baseResponse = new BaseResponse();
        baseResponse.success(team);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping
    public ResponseEntity<?> getTeams(){
        List<Team> team = teamService.getTeamList();
        baseResponse = new BaseResponse();
        baseResponse.success(team);
        return ResponseEntity.ok(baseResponse);
    }

    @GetMapping("/{teamId}")
    public ResponseEntity<?> getTeam(@PathVariable Integer teamId){
        Team team = teamService.getTeam(teamId);
        baseResponse = new BaseResponse();
        baseResponse.success(team);
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping("/{teamId}")
    public ResponseEntity<?> updateTeam(@PathVariable Integer teamId,@RequestBody Team request){
        Team team = teamService.updateTeam(teamId,request);
        baseResponse = new BaseResponse();
        baseResponse.success(team);
        return ResponseEntity.ok(baseResponse);
    }

    @DeleteMapping("/{teamId}")
    public ResponseEntity<?> deleteTeam(@PathVariable Integer teamId){
        teamService.deleteTeam(teamId);
        baseResponse = new BaseResponse();
        baseResponse.success("Team ID: %s is deleted".formatted(teamId));
        return ResponseEntity.ok(baseResponse);
    }

    @PutMapping(value = "/upload/image", consumes = {"multipart/form-data"})
    public ResponseEntity<?> createTeam(
            @RequestParam Integer teamId,
            @RequestParam MultipartFile file
    ){
        String uploadPhoto = teamService.uploadPhoto(teamId, file);
        return ResponseEntity.ok(uploadPhoto);
    }

    @Hidden
    @GetMapping(path = "/image/{filename}",produces = {IMAGE_PNG_VALUE,IMAGE_JPEG_VALUE})
    public byte[] getProfile(@PathVariable("filename") String filename) throws Exception{
        return Files.readAllBytes(Paths.get(serverPath + filename));
    }
}
