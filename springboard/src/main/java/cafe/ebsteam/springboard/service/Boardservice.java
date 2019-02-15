package cafe.ebsteam.springboard.service;

import java.util.HashMap;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cafe.ebsteam.springboard.mapper.BoardMapper;
import cafe.ebsteam.springboard.vo.Board;

@Service
//service : 하나라도 실패하면 전체가 취소 된다. 트랜잭션 기능을 가짐

@Transactional
//서비스 실행하다 예외가 발생하면 rollback시켜줌
public class Boardservice {
	
	@Autowired	private BoardMapper boardMapper;
	
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
	
	public int addBoard(Board board) {
	
		return boardMapper.insertBoard(board);	
	}
	
	public int removeBoard(Board board) {
		return boardMapper.deleteBoard(board);	
	}
	public int modifyBoard(Board board) {
		return boardMapper.updateBoard(board);	
	}
	
	
	
	
	
}
