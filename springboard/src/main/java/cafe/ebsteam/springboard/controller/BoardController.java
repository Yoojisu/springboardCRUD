package cafe.ebsteam.springboard.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import cafe.ebsteam.springboard.service.Boardservice;
import cafe.ebsteam.springboard.vo.Board;

@Controller
public class BoardController {

	@Autowired
	private Boardservice boardService;

	// 입력 폼
	@GetMapping("/addBoard")
	public String addBoard() {
		System.out.println("입력 폼 실행");
		// addList경로를 get방식으로 받으면 addList.html으로 이동한다.(입력 폼이 있음)
		return "addBoard";
	}

	// 입력 액션
	@PostMapping("/addBoard")
	public String addBoard(Board board) {
		// addList화면에서 값들을 입력받으면 post방식으로 addList경로를 보낸다.
		// 그 경로로 들어오면 addBord()실행
		System.out.println("입력 액션 실행");

		boardService.addBoard(board);
		// boardService내의 addBoard메서드레 board를 매개변수로 받아 호출한다. 입력하는 메서드
		//경로를 boardList로 리다이렉트 한다.
		return "redirect:/boardList";
	}

	// 리스트 요청

	@GetMapping("/boardList")
	public String boardList(Model model,
			@RequestParam(value = "currentPage", required = false, defaultValue = "1") int currentPage) {

		// required=false, defaultValue="1" : currentPage 값을 넘겨받지 않으면 1으로 셋팅하겠다는 뜻
		// int currentPage : currentPage를 int로 형변환 해줌

		// @RequestParam(value="currentPage")에서 currentPage생략가능 int currentPage와 변수의 이름이
		// 같기 때문! 하지만 아직은 생략하지 말자!
		System.out.println("리스트 출력");

		Map<String, Object> list = (Map<String, Object>) boardService.selectBoardList(currentPage);
		// list.get(boardService);

		model.addAttribute("boardList", list);
		model.addAttribute("currentPage", currentPage);
		model.addAttribute("lastPage", list.get("lastPage"));

		System.out.println(currentPage + "<-currentPage 출력 값");
		System.out.println(list + "<-List 출력 값");

		// board_list->타임리프로 넘기기 (html)
		// /boardList경로가 요청되면 boardList.html로 이동
		return "boardList";
	}

	// 삭제 폼

	@GetMapping("/removeBoard")
	public String removeForm(Model model,@RequestParam(value = "boardNo") int boardNo) {
		System.out.println("삭제 폼 실행");
		Board list = boardService.getBoard(boardNo);
		model.addAttribute("list", list);
		// /removeBoard경로가 요청되면 removeBoard.html로 이동
		return "removeBoard";

	}

	// 삭제 액션
	@PostMapping("/removeBoard")
	public String removeBoard(Board board) {
		System.out.println("삭제 액션 실행");
		
		boardService.removeBoard(board);
		//경로를 boardList로 리다이렉트 한다.
		return "redirect:/boardList";
	}

	// 수정 폼
	@GetMapping("/modifyBoard")
	public String modifyBoard(Model model, @RequestParam(value = "boardNo") int boardNo) {
		Board list = boardService.getBoard(boardNo);
		//getBoard메서드에 boardNo매개변수를 입력한 후 리턴값을 list에 저장한다.
		model.addAttribute("list", list);
		//Board타입으로 저장된 list를 "list"변수에 setting하여 호출한 곳에서 꺼내 쓸 수 있게 한다.
		System.out.println("수정 폼 boardNo:" + boardNo);
		System.out.println("수정 폼 실행");
		
		// /modifyBoard경로가 요청되면 modifyBoard.html로 이동
		return "modifyBoard";
	}

	// 수정 액션
	@PostMapping("/modifyBoard")
	public String modifyBoard(Board board) {
		
		System.out.println("수정 액션 실행");
		
		boardService.modifyBoard(board);
		//modifyBoard메서드 실행
		//경로를 boardList로 리다이렉트 한다.
		return "redirect:/boardList";

	}

}
