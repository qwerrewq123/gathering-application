package spring.myproject.service.board;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.domain.Board;
import spring.myproject.dto.request.board.AddBoardRequest;
import spring.myproject.dto.response.board.*;
import spring.myproject.exception.board.NotFoundBoardException;
import spring.myproject.repository.board.BoardRepository;
import spring.myproject.repository.enrollment.EnrollmentRepository;
import spring.myproject.domain.Gathering;
import spring.myproject.exception.gathering.NotFoundGatheringException;
import spring.myproject.repository.gathering.GatheringRepository;
import spring.myproject.domain.Image;
import spring.myproject.repository.image.ImageRepository;
import spring.myproject.exception.meeting.NotAuthorizeException;
import spring.myproject.domain.User;
import spring.myproject.exception.user.NotFoundUserException;
import spring.myproject.repository.user.UserRepository;
import spring.myproject.s3.S3ImageDownloadService;
import spring.myproject.s3.S3ImageUploadService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static spring.myproject.util.ConstClass.*;

@Service
@RequiredArgsConstructor
@Transactional
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDownloadService s3ImageDownloadService;
    private final GatheringRepository gatheringRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final ImageRepository imageRepository;
    @Value("${server.url}")
    private String url;

    public BoardResponse fetchBoard(Long gatheringId, Long boardId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("no exist User!!"));
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));
        if(enrollmentRepository.findByGatheringAndEnrolledBy(gathering,user).isEmpty()) throw new NotAuthorizeException("no Authorize to fetch board");
        List<BoardQuery> boardQueries = boardRepository.fetchBoard(boardId);
        if(boardQueries.isEmpty()) throw new NotFoundBoardException("no board found");
        String userImageUrl = getUserImageUrl(boardQueries);
        List<String> imageUrls = getImageUrls(boardQueries);
        return BoardResponse.of(boardQueries,imageUrls,userImageUrl,SUCCESS_CODE,SUCCESS_MESSAGE);
    }

    public AddBoardResponse addBoard(String username, AddBoardRequest addBoardRequest, List<MultipartFile> files, Long gatheringId) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));
        if(enrollmentRepository.findByGatheringAndEnrolledBy(gathering,user).isEmpty()) throw new NotAuthorizeException("no Authorize to add board");
        Board board = AddBoardRequest.of(addBoardRequest,user,gathering);
        List<Image> images = saveImages(files,board,gathering);
        boardRepository.save(board);
        imageRepository.saveAll(images);
        return AddBoardResponse.of(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public BoardsResponse fetchBoards(Long gatheringId, String username, Integer pageNum, Integer pageSize) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new NotFoundUserException("no exist User!!"));
        Gathering gathering = gatheringRepository.findById(gatheringId).orElseThrow(() -> new NotFoundGatheringException("no exist Gathering!!"));
        if(enrollmentRepository.findByGatheringAndEnrolledBy(gathering,user).isEmpty()) throw new NotAuthorizeException("no Authorize to fetch board");
        PageRequest pageRequest = PageRequest.of(pageNum, pageSize);
        Page<BoardsQuery> page = boardRepository.fetchBoards(pageRequest);
        return BoardsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
    }

    private String getUserImageUrl(List<BoardQuery> boardQueries){

        return getUrl(boardQueries.getFirst().getUserImageUrl());

    }

    private List<String> getImageUrls(List<BoardQuery> boardQueries) {
        List<String> imageUrls = new ArrayList<>();
        for (BoardQuery boardQuery : boardQueries) {
                imageUrls.add(getUrl(boardQuery.getImageUrl()));
        }
        return imageUrls;
    }

    private String getUrl(String fileUrl){
        return url+fileUrl;
    }

    private List<Image> saveImages(List<MultipartFile> files,Board board,Gathering gathering) throws IOException {
        List<Image> images = new ArrayList<>();
        for(MultipartFile file : files){
            if(!file.isEmpty()){
                String url = s3ImageUploadService.upload(file);
                if(StringUtils.hasText(url)){         Image image = Image.builder()
                        .url(url)
                        .board(board)
                        .gathering(gathering)
                        .build();
                    images.add(image);

                }
            }
        }
        return images;
    }

}
