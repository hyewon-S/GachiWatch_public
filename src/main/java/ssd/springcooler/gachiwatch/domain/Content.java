package ssd.springcooler.gachiwatch.domain;//콘텐츠에 관한 정보

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Entity
@Setter
public class Content {
	@Id
	@Column(name = "content_id")
	private int contentId;
	private String title;
	private String intro;
	@Column(name = "casts")
	private String cast;
	private Double rate;
	@Column(name = "upload_date")
	private String uploadDate;
	@Column(name = "content_type")
	private String contentType;
	@Column(name = "img_url")
	private String imgUrl;

	@ElementCollection
	@CollectionTable(name = "content_genre", joinColumns = @JoinColumn(name = "genre_id"))
	@Column(name = "genre")
	private List<Integer> genre;

	@ElementCollection
	@CollectionTable(name = "content_platform", joinColumns = @JoinColumn(name = "content_id"))
	@Column(name = "platform")
	private List<Integer> platform;

	public Content() {}

	//Mapper 때문에 필요함...
	public Content(int id, String title, String overview, String casts, double rating, String releaseDate, String contentType, String imageUrl) {
		this.contentId = id;
		this.title = title;
		this.intro = overview;
		this.cast = casts;
		this.rate = rating;
		this.uploadDate = releaseDate;
		this.contentType = contentType;
		this.imgUrl = imageUrl;
	}

	//TMDB API 불려올때 필요함..
	public Content(int id, String title, String overview, List<Integer> genres, String string, List<Integer> platforms, double rating, String releaseDate, String imageUrl, String type) {
		this.title = title;
		this.intro = overview;
		this.genre = genres;
		this.cast = string;
		this.rate = rating;
		this.uploadDate = releaseDate;
		imgUrl = imageUrl;
		platform = platforms;
		contentId = id;
		contentType = type;
    }


	@Override
	public String toString() {
		return "title: " + title + "intro : " + intro + "\ncast : " + cast + ", rate : " + rate +
				", uploadDate : " + uploadDate +  ", contentType : " + contentType + ", genre : " + genre + "platform : " + platform + "\n";
	}

	public int getContentId() { return contentId;
	}

	public void setGenre(List<Integer> genres) { this.genre = genres;
	}

	public void setPlatform(List<Integer> platforms) { this.platform = platforms;
	}

	public List<Integer> getGenre() {
		return genre;
	}

	public List<Integer> getPlatform() {
		return platform;
	}
}
