package spring.myproject.board.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import spring.myproject.board.Board;
import spring.myproject.board.dto.request.AddBoardRequest;
import spring.myproject.board.dto.response.AddBoardResponse;
import spring.myproject.board.dto.response.FetchBoardResponse;
import spring.myproject.board.dto.response.FetchBoardsResponse;
import spring.myproject.board.dto.response.QueryBoardDto;
import spring.myproject.board.exception.NotFoundBoardException;
import spring.myproject.board.repository.BoardRepository;
import spring.myproject.domain.image.Image;
import spring.myproject.domain.image.repository.ImageRepository;
import spring.myproject.domain.meeting.Meeting;
import spring.myproject.domain.meeting.exception.NotAuthorizeException;
import spring.myproject.domain.meeting.exception.NotFoundMeetingExeption;
import spring.myproject.domain.meeting.repository.MeetingRepository;
import spring.myproject.domain.user.User;
import spring.myproject.domain.user.exception.NotFoundUserException;
import spring.myproject.domain.user.repository.UserRepository;
import spring.myproject.s3.S3ImageDownloadService;
import spring.myproject.s3.S3ImageUploadService;
import spring.myproject.util.ConstClass;

import java.io.IOException;

import static spring.myproject.util.ConstClass.*;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final UserRepository userRepository;
    private final MeetingRepository meetingRepository;
    private final S3ImageUploadService s3ImageUploadService;
    private final S3ImageDownloadService s3ImageDownloadService;
    private final ImageRepository imageRepository;

    public FetchBoardResponse fetchBoard(Long boardId, String username) {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Board fetchBoard = boardRepository.fetchBoard(boardId).orElseThrow(()-> new NotFoundBoardException("no found Board!!"));
        FetchBoardResponse fetchBoardResponse = FetchBoardResponse.of(fetchBoard,SUCCESS_CODE,SUCCESS_MESSAGE);
        return fetchBoardResponse;
    }

    public AddBoardResponse addBoard(String username, AddBoardRequest addBoardRequest, MultipartFile file,Long meetingId) throws IOException {
        User user = userRepository.findByUsername(username).orElseThrow(()->new NotFoundUserException("no exist User!!"));
        Meeting meeting = meetingRepository.findById(meetingId).orElseThrow(() -> new NotFoundMeetingExeption("no exist Meeting!!"));
        if(!meeting.getAttends().contains(user)){
            throw new NotAuthorizeException("no Authorize to add board");
        }
        Image image = null;
        if(!file.isEmpty()){
            String url = s3ImageUploadService.upload(file);
            if(StringUtils.hasText(url)){
                image = Image.builder()
                        .url(url)
                        .build();
            }
        }
        imageRepository.save(image);
        Board board = AddBoardRequest.of(addBoardRequest,user,meeting);
        boardRepository.save(board);
        return AddBoardResponse.of(SUCCESS_CODE, SUCCESS_MESSAGE);
    }

    public FetchBoardsResponse fetchBoards(String username,Integer pageNum) {
        PageRequest pageRequest = PageRequest.of(pageNum, 5);
        Page<QueryBoardDto> page = boardRepository.fetchBoards(pageRequest);
        return FetchBoardsResponse.of(SUCCESS_CODE,SUCCESS_MESSAGE,page);
    }
}
