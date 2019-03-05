package cafe.ebsteam.springboard.mapper;

import org.apache.ibatis.annotations.Mapper;

import cafe.ebsteam.springboard.vo.Boardfile;

@Mapper
public interface BoardfileMapper {

	int insertBoardFile(Boardfile boardfile);
	
}
