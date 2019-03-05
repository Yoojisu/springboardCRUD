package cafe.ebsteam.springboard.service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import cafe.ebsteam.springboard.mapper.BoardMapper;
import cafe.ebsteam.springboard.mapper.BoardfileMapper;
import cafe.ebsteam.springboard.vo.Board;
import cafe.ebsteam.springboard.vo.BoardRequest;
import cafe.ebsteam.springboard.vo.Boardfile;


//service : 하나라도 실패하면 전체가 취소 된다. 트랜잭션 기능을 가짐

//서비스 실행하다 예외가 발생하면 rollback시켜줌
@Service
@Transactional
public class Boardservice {
	
	@Autowired private BoardMapper boardMapper;
	@Autowired private BoardfileMapper boardfileMapper;
	
	public Board getBoard(int boardNo) {
		
		return boardMapper.selectBoard(boardNo);
	}

	public Map<String, Object> selectBoardList(int currentPage){
		//컨트롤러에서 map을 받지 않음 (Map<String, Integer> map)
		//int로 받아 map에 넣어 줘야 함!
		//(int currentPage, int rowPerPage) 컨트롤러에서는 rowPerPage을 만드는 방법은 좋은 방법이 아님 
		//requestParameter로 값을 받던지 BoardService.java에 입력해준다.
		
		//1.
		
		final int ROW_PER_PAGE = 10;
		Map<String, Integer> map = new HashMap<String, Integer>();
		int startPage = (currentPage-1)*10;
		map.put("currentPage", currentPage);
		map.put("rowPerPage", ROW_PER_PAGE);
		map.put("startPage", startPage);
	
		//2.
		
		int boardCount = boardMapper.selectBoardCount();
		int lastPage = (int)(Math.ceil(boardCount / ROW_PER_PAGE));
		
		
		//리턴은 하나만 받을 수 있기 때문에 Map을 생성하여 그 안에 사용할 값들을 다 저장한 후 리턴한다.
		Map<String, Object> returnMap = new  HashMap<String, Object>();
		
		returnMap.put("list",boardMapper.selectBoardList(map));
		returnMap.put("boardCount", boardCount);
		returnMap.put("currentPage", currentPage);
		returnMap.put("rowPerPage", ROW_PER_PAGE);
		returnMap.put("lastPage", lastPage);
		
		return returnMap;
	
	}
	
	
	public int getBoardCount() {
		return boardMapper.selectBoardCount();	
		
	}
	
	public void addBoard(BoardRequest boardRequest, String path)  {
		/* 서비스에서 처리해야 할 일들
		 *  1.BoardRequest를 분리 : board, file, file의 정보
		 *  2.board->boardVo
		 *  3.file정보->boardFileVo
		 *  4.file-> path를 이용해 물리적 장치 저장 
		 */  
		//@Transactional필요함 둘중에 하나가 실패되면 모두가 취소가 되야 할 때 사용
		//@Transactional 하나라도 실패하면 롤백하는 것
		//1.
		Board board = new Board();
		//화면에서 입력받은 값들을 board내에 setting
		board.setBoardPw(boardRequest.getBoardPw());
		board.setBoardTitle(boardRequest.getBoardTitle());				
		board.setBoardUser(boardRequest.getBoardUser());
		board.setBoardContent(boardRequest.getBoardContent());				
		
		System.out.println("board : "+board);
		//새로운 데이터를 입력해야 boardNo가 생성되므로 setting위에 작성해야한다.
		//board.getBoardNo()<-입력전에는 null  (카페 1280번째 글 참조)
		boardMapper.insertBoard(board);
		
		System.out.println("boardNo-->"+board.getBoardNo());
		//insert메서드 실행 후 no가 자동으로 생성된다. 자동생성된 no를 boardRequest 객체의 boardNo에 setting한다.
		//2.
	
		//파일 이름 가져오기(배열) file
		//화면에서 입력받은 file의 정보를 list로 저장
		List<MultipartFile> files = boardRequest.getFiles();
		for(MultipartFile f : files) {
			//f - >boardfile
			//전체 작업이 롤백되면 파일삭제 작업은 직접하자!(예외,if 등...)
			Boardfile boardfile = new Boardfile();
			//boardfile내에 화면에서 저장한 파일의 정보(size,contentType,ext,fileName)들을 setting
			boardfile.setBoardNo(board.getBoardNo());
			boardfile.setFileSize(f.getSize());
			boardfile.setFileType(f.getContentType());
			
			//getOriginalFilename : 파일이름을 출력해주는 메서드
			String originalFilename = f.getOriginalFilename();
			System.out.println("originalFilename : "+originalFilename);
			
			//파일명에서 맨뒤의 .xlsx 부터의 .을 찾기위해서 lastIndexOf를 사용
			int i = originalFilename.lastIndexOf(".");
			
			//두번째 글자부터 짤라달라..
			String ext = originalFilename.substring(i+1);
			boardfile.setFileExt(ext);
			String fileName = UUID.randomUUID().toString();
			boardfile.setFileName(fileName);
			System.out.println("Boardfile : "+boardfile);
			boardfileMapper.insertBoardFile(boardfile);
			
			//3.파일저장
			try {
				f.transferTo(new File(path+"/"+fileName+"."+ext));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		}
	
		
	}
	
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);	
	}
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);	
	}
	
	
		
	
}
