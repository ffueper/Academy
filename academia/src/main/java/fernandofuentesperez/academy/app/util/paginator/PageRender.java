package fernandofuentesperez.academy.app.util.paginator;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;

public class PageRender <T>{

	private String url;
	private Page<T> page;
	
	private int totalPages;
	
	private int numElementForPage;
	
	private int currentPage;
	
	private List<PageItem> pages;
	
	public PageRender(String url, Page<T> page) {
		this.url = url;
		this.page = page;
		this.pages = new ArrayList<PageItem>();
		
		numElementForPage = page.getSize();
		totalPages = page.getTotalPages();
		currentPage = page.getNumber() + 1; //Empieza desde 0, por eso le sumamos 1
		
		int since, until;
		
		if(totalPages <= numElementForPage) {
			since = 1;
			until = totalPages;
		} else {
			if( currentPage <= numElementForPage/2 ) {
				since = 1;
				until = numElementForPage;
			} else if( currentPage >= totalPages - numElementForPage/2 ) {
				since = totalPages - numElementForPage + 1;
				until = numElementForPage;
			} else {
				since = currentPage - numElementForPage/2;
				until = numElementForPage;
			}
		}
		
		for(int  i=0; i < until; i++) {
			pages.add( new PageItem( since + i, currentPage == since+i ) );
		}
		
	}
	
	public boolean isFirst() {
		return page.isFirst();
	}
	
	public boolean isLast() {
		return page.isLast();
	}
	
	public boolean isHasNext() {
		return page.hasNext();
	}
	
	public boolean isHasPrevious() {
		return page.hasPrevious();
	}

	public String getUrl() {
		return url;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public List<PageItem> getPages() {
		return pages;
	}
	
}
