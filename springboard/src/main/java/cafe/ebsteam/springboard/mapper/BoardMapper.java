package cafe.ebsteam.springboard.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import cafe.ebsteam.springboard.vo.Board;
@Mapper
public interface BoardMapper {
	
	//annotation을 이용하여 여기에 바로 쿼리를 만들 수도 있음! 하지만 우리는 boardMapper.xml에 만들 것임
	
	Board selectBoard(int boardNo);
	
	List<Board> selectBoardList(Map<String, Integer> map);
	//List<Board> getBoardList(int currentPage, int pagePerRow)-> 두개를 한꺼번에 보낼 수 없다!하나로 묶어서 보내야 한다.
	//페이징 되고 있음

	int selectBoardCount();
	//페이징 하기 위해 전체글자가 몇개인지 알아보는 메서드
	
	int insertBoard(Board board);
	//입력 메서드
	
	int updateBoard(Board board);
	//수정 메서드
	
	int deleteBoard(Board board);
	//int deleteBoard(int boardNo, String boardPw) ->mybatis는 두개를 한꺼번에 보낼 수 없다!하나로 묶어서 보내야 함 (Board로 묶음)
	//삭제 메서드
	
	


}
