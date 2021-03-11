package com.lovemesomecoding.pizzaria.dto;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CustomPage<T> implements Serializable {

	private static final long serialVersionUID = 1L;

	private int page;
	private int totalPages;
	private long totalElements;
	private int size;
	private boolean last;
	private boolean first;
	private List<T> content;

	@JsonIgnore
	private Page actualPage;

	public CustomPage() {
	}

	public CustomPage(Page jpaPage) {
		this(jpaPage.getNumber(), jpaPage.getTotalPages(), jpaPage.getTotalElements(), jpaPage.getSize(),
				jpaPage.isLast(), jpaPage.isFirst());
		this.setContent(jpaPage.getContent());
		this.actualPage = jpaPage;
	}

	public CustomPage(int page, int totalPages, long totalElements, int size, boolean last, boolean first) {
		super();
		this.page = page;
		this.totalPages = totalPages;
		this.totalElements = totalElements;
		this.size = size;
		this.last = last;
		this.first = first;
	}

	public void printActualPage() {
		System.out.println(actualPage.toString());
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getTotalPages() {
		return totalPages;
	}

	public void setTotalPages(int totalPages) {
		this.totalPages = totalPages;
	}

	public long getTotalElements() {
		return totalElements;
	}

	public void setTotalElements(long totalElements) {
		this.totalElements = totalElements;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public boolean isLast() {
		return last;
	}

	public void setLast(boolean last) {
		this.last = last;
	}

	public boolean isFirst() {
		return first;
	}

	public void setFirst(boolean first) {
		this.first = first;
	}

	public List<T> getContent() {
		return content;
	}

	public void setContent(List<T> content) {
		this.content = content;
	}
}
