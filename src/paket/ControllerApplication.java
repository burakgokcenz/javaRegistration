package paket;

import java.net.URL;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class ControllerApplication implements Initializable {

    @FXML
    private Spinner<Integer> SpnÝd;
    
    @FXML
    private TextField txtAd;

    @FXML
    private TextField txtSoyad;

    @FXML
    private DatePicker DateTarih;

    @FXML
    private Spinner<Double> SpnMaas;

    @FXML
    private ComboBox<String> cmbCinsiyet;

    @FXML
    private ComboBox<String> cmbDepart;

    @FXML
    private Button btnPersonel;

    @FXML
    private Button btnGuncel;

    @FXML
    private Button btnSil;

    @FXML
    private TableView<Personel> TableView;
    
    @FXML
    private TableColumn<Personel, Integer> ColÝd;

    @FXML
    private TableColumn<Personel,String> ColAd;

    @FXML
    private TableColumn<Personel,String> ColSoyad;

    @FXML
    private TableColumn<Personel, LocalDate> ColTarih;

    @FXML
    private TableColumn<Personel, Integer> ColMaas;

    @FXML
    private TableColumn<Personel,String> ColCinsiyet;

    @FXML
    private TableColumn<Personel, String> ColDepart;
    private ObservableList<Personel> personel=FXCollections.observableArrayList();
    private Alert hatamesajý = new Alert(AlertType.ERROR);
    private Alert bilgilendirmemesajý=new Alert(AlertType.INFORMATION);
    
    private PreparedStatement sorgu;
    private ResultSet sonuçkümesi; 
    private Connection baðlantý;
    
    @FXML
    void KayitEkle(ActionEvent event) {
    	LocalDate bugün = LocalDate.now();
        int fark= bugün.getYear() - DateTarih.getValue().getYear();
        if(DateTarih.getValue().getMonthValue() == bugün.getMonthValue()) {
            if(bugün.getDayOfMonth() < DateTarih.getValue().getDayOfMonth()) {
                fark = fark-1;
            }
        } else if(bugün.getMonthValue() < DateTarih.getValue().getMonthValue()) {
            fark = fark-1;
        }
        if(fark < 18) {
            hatamesajý.setTitle("HATA");
            hatamesajý.setHeaderText("18 yaþýndan küçük personel olamaz!");
            hatamesajý.show();
            DateTarih.setValue(LocalDate.of(2003, 1, 1));
            return;
        }
        
            int id=SpnÝd.getValue();
        	for (int i = 0; i < personel.size(); i++) {    		
        		if (id==personel.get(i).getId()) {
    				hatamesajý.setTitle("Hata");
    				hatamesajý.setHeaderText("Ayný id de personel zaten kayýtlý");
    				hatamesajý.show();
    				return;					
    			}
    			
    		}
        	String ad=txtAd.getText();
    		String soyad=txtSoyad.getText();
    		LocalDate tarih=DateTarih.getValue();
    		Double maas=SpnMaas.getValue();
    		String cinsiyet=cmbCinsiyet.getValue();
    		String depart=cmbDepart.getValue();
    		
        	
        	
      
        	try {
        		
            	
    			sorgu=baðlantý.prepareStatement("insert into PersonelTbl"
    		+ "(id,ad,soyad,dogumtarihi,maas,cinsiyet,departman) values (?,?,?,?,?,?,?)");
    			sorgu.setInt(1, id);
    			sorgu.setString(2, ad);
    			sorgu.setString(3, soyad);
    			sorgu.setObject(4, tarih);
    			sorgu.setDouble(5, maas);
    			sorgu.setString(6, cinsiyet);
    			sorgu.setString(7, depart);
    			
    			sorgu.execute();
    			Personel personeller=new Personel(id, ad, soyad, tarih, maas, cinsiyet, depart);
            	personel.add(personeller);
            	
    			bilgilendirmemesajý.setTitle("Kayýt Ýþlemi");
    			bilgilendirmemesajý.setHeaderText("Kayýt iþlemi baþarýyla gerçekleþti");
    			bilgilendirmemesajý.show();
    			
    		} catch (SQLException e) {
    			hatamesajý.setTitle("Veri tabanýna kayýt hatasý");
    			hatamesajý.setHeaderText("Veritabanýna kayýt sýrasýnda bir hata meydana geldi");
    			hatamesajý.show();
    		}
        	

        }
    	

    

    @FXML
    void KayitSil(ActionEvent event) {

    	int index=TableView.getSelectionModel().getSelectedIndex();
    	if (index>=0) { 
    		try {
    			sorgu=baðlantý.prepareStatement("delete from PersonelTbl where id="+"'"+personel.get(index).getId()+"'");
				personel.remove(index);
				sorgu.executeUpdate();
				bilgilendirmemesajý.setTitle("Silme iþlemi");
				bilgilendirmemesajý.setHeaderText("Silme iþlemi baþarýyla gerçekleþti");
				bilgilendirmemesajý.show();
			} catch (SQLException e) {
				hatamesajý.setTitle("Silme hatasý");
				hatamesajý.setHeaderText("Silme iþleminde hata oluþtu");
				hatamesajý.show();
			}
    	}


    }

    @FXML
    void btnGuncelle(ActionEvent event) {
    	int index=TableView.getSelectionModel().getSelectedIndex();
    	if (index!=-1) {
    		int id=SpnÝd.getValue();
    		for (int i = 0; i < personel.size(); i++) {      		
    			if (id==personel.get(i).getId()&&id!=personel.get(index).getId()) {				
    				hatamesajý.setTitle("Hata");
    				hatamesajý.setHeaderText("Personel zaten kayýtlý");
    				hatamesajý.show();
    				return;		
    			}
    			
    		}
    		String ad=txtAd.getText();
    		String soyad=txtSoyad.getText();
    		LocalDate tarih=DateTarih.getValue();
    		Double maas=SpnMaas.getValue();
    		String cinsiyet=cmbCinsiyet.getValue();
    		String depart=cmbDepart.getValue();
    	try {
    		sorgu=baðlantý.prepareStatement("update PersonelTbl set id=?,ad=?,soyad=?,dogumtarihi=?,maas=?,cinsiyet=?,departman=? where id="+"'"+personel.get(index).getId()+"'");
			sorgu.setInt(1, id);
			sorgu.setString(2, ad);
			sorgu.setString(3, soyad);
			sorgu.setObject(4, tarih);
			sorgu.setDouble(5, maas);
			sorgu.setString(6, cinsiyet);
			sorgu.setString(7, depart);
			
			sorgu.executeUpdate();
			Personel personeller=new Personel(id, ad, soyad, tarih, maas, cinsiyet, depart);
    		personel.set(index,personeller);
			bilgilendirmemesajý.setTitle("Güncelleme Ýþlemi");
			bilgilendirmemesajý.setHeaderText("Güncelleme iþlemi baþarýyla tamamlandý");
			bilgilendirmemesajý.show();
		} catch (Exception e) {
			hatamesajý.setTitle("Güncelleme hatasý");
			hatamesajý.setHeaderText("Güncelleme sýrasýnda hata meydana geldi");
			hatamesajý.show();
		}
    	}
    }
    		
    		
    

    
    			

				
				
				
			


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		SpnÝd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1));
		SpnMaas.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(2200, 15000, 5000, 0.1));
		
		cmbCinsiyet.getItems().add("Erkek");
        cmbCinsiyet.getItems().add("Kadýn");
        
        cmbDepart.getItems().add("Araç Satýþ");
        cmbDepart.getItems().add("Biliþim");
        cmbDepart.getItems().add("Ýnsan Kaynaklarý");
        
		
        ColÝd.setCellValueFactory(new PropertyValueFactory<>("id"));
		ColAd.setCellValueFactory(new PropertyValueFactory<>("ad"));
		ColSoyad.setCellValueFactory(new PropertyValueFactory<>("soyad"));
		ColTarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));
		ColMaas.setCellValueFactory(new PropertyValueFactory<>("maas"));
		ColCinsiyet.setCellValueFactory(new PropertyValueFactory<>("cinsiyet"));
		ColDepart.setCellValueFactory(new PropertyValueFactory<>("depart"));
		
         TableView.setItems(personel);
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
			baðlantý=DriverManager.getConnection("jdbc:sqlserver://DESKTOP-CHTADSR\\SQLEXPRESS;database=Personel Bilgileri;integratedSecurity=true");
		} catch (Exception e) {
			hatamesajý.setTitle("Driver Hatasý");
			hatamesajý.setHeaderText("Hata meydana geldi baðlantýnýzý kontrol edin");
			hatamesajý.show();
			e.printStackTrace();
		}
		
		TableView.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>() {

			@Override

			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				
				int index=newValue.intValue();
				if(index>=0) {
					txtAd.setText(personel.get(index).getAd());
					txtSoyad.setText(personel.get(index).getSoyad());
					DateTarih.setValue(personel.get(index).getTarih());
					SpnMaas.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(2200, 15000, personel.get(index).getMaas(), 0.1));
					SpnÝd.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, personel.get(index).getId(), 1));
					cmbCinsiyet.setValue(personel.get(index).getCinsiyet());
					cmbDepart.setValue(personel.get(index).getDepart());
					
				}				
			}
		});
		try {
			sorgu=baðlantý.prepareStatement("select * from PersonelTbl");
			sonuçkümesi=sorgu.executeQuery();
			while (sonuçkümesi.next()) {
				
				int id=sonuçkümesi.getInt(1);
				String ad=sonuçkümesi.getString(2);
				String soyad=sonuçkümesi.getString(3);
				LocalDate tarih=sonuçkümesi.getDate(4).toLocalDate();
				Double maas=sonuçkümesi.getDouble(5);
				String cinsiyet=sonuçkümesi.getString(6);
				String depart=sonuçkümesi.getString(7);
				
				Personel personeller= new Personel(id, ad, soyad, tarih, maas, cinsiyet, depart);
				personel.add(personeller);
				
			}
			bilgilendirmemesajý.setTitle("Veritabanýndan yükleme");
			bilgilendirmemesajý.setHeaderText("Veritabanýndan bilgiler yüklendi");
			bilgilendirmemesajý.show();
		} catch (SQLException e) {
			hatamesajý.setTitle("Yükleme hatasý");
			hatamesajý.setHeaderText("Veritabanýndan yükleme sýrasýnda bir hata meydana geldi");
			hatamesajý.show();
		}
		
		
	}

}