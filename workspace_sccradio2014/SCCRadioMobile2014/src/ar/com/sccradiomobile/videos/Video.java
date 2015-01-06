package ar.com.sccradiomobile.videos;


public class Video {
	private final String title;
	private final String url;
	private final String id;
	private final String thumbUrl;
	private final String dateUploaded;
	private final String description;
	private final float duracion;

    public String getId() {
        return id;
    }

    public Video(final String id, final String title, final String url, String thumbUrl, float duration, String dateUploaded, String description) {
    		super();
    		this.title = title;
    		this.url = url;
            this.id = id;
            this.thumbUrl = thumbUrl;
            this.duracion = duration;
            this.dateUploaded = dateUploaded;
            this.description = description;
    	}
 
 // getter & setter
 
	@Override
	public String toString() {
		return title;
	}

	public String getTitle() {
		return title;
	}

	public String getUrl() {
		return url;
	}

    public float getDuracion() {
        return duracion;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getDuracionDetalle() {
        String rval;
        int num,hor,min,seg;
        hor = (int) (duracion/3600);

        min= (int) ((duracion-(3600*hor))/60);
        seg= (int) (duracion-((hor*3600)+(min*60)));
        StringBuilder sb = new StringBuilder();
        if (hor!=0) {
           sb.append(hor+"h ");
        }
        if (min !=0) {
            sb.append(min+"m ");
        }
        if (seg !=0) {
            sb.append(seg+"s ");
        }
        return sb.toString();
    }

    public String getDateUploaded() {
        return dateUploaded;
    }
}
