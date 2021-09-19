package paket;

import java.time.LocalDate;

public class Personel {
	private String ad,soyad,cinsiyet,depart;
	private Double maas;
	private int id;
	private LocalDate tarih;
	public Personel(int id,String ad, String soyad,LocalDate tarih,Double maas,String cinsiyet, String depart) {
		super();
		this.id = id;
		this.ad = ad;
		this.soyad = soyad;
		this.tarih = tarih;
		this.maas = maas;
		this.cinsiyet = cinsiyet;
		this.depart = depart;
		
		
	}
	public String getAd() {
		return ad;
	}
	public void setAd(String ad) {
		this.ad = ad;
	}
	public String getSoyad() {
		return soyad;
	}
	public void setSoyad(String soyad) {
		this.soyad = soyad;
	}
	public String getCinsiyet() {
		return cinsiyet;
	}
	public void setCinsiyet(String cinsiyet) {
		this.cinsiyet = cinsiyet;
	}
	public String getDepart() {
		return depart;
	}
	public void setDepart(String depart) {
		this.depart = depart;
	}
	public Double getMaas() {
		return maas;
	}
	public void setMaas(Double maas) {
		this.maas = maas;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public LocalDate getTarih() {
		return tarih;
	}
	public void setTarih(LocalDate tarih) {
		this.tarih = tarih;
	}

}