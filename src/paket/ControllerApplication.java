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
    private Spinner<Integer> Spn�d;
    
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
    private TableColumn<Personel, Integer> Col�d;

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
    private Alert hatamesaj� = new Alert(AlertType.ERROR);
    private Alert bilgilendirmemesaj�=new Alert(AlertType.INFORMATION);
    
    private PreparedStatement sorgu;
    private ResultSet sonu�k�mesi; 
    private Connection ba�lant�;
    
    @FXML
    void KayitEkle(ActionEvent event) {
    	LocalDate bug�n = LocalDate.now();
        int fark= bug�n.getYear() - DateTarih.getValue().getYear();
        if(DateTarih.getValue().getMonthValue() == bug�n.getMonthValue()) {
            if(bug�n.getDayOfMonth() < DateTarih.getValue().getDayOfMonth()) {
                fark = fark-1;
            }
        } else if(bug�n.getMonthValue() < DateTarih.getValue().getMonthValue()) {
            fark = fark-1;
        }
        if(fark < 18) {
            hatamesaj�.setTitle("HATA");
            hatamesaj�.setHeaderText("18 ya��ndan k���k personel olamaz!");
            hatamesaj�.show();
            DateTarih.setValue(LocalDate.of(2003, 1, 1));
            return;
        }
        
            int id=Spn�d.getValue();
        	for (int i = 0; i < personel.size(); i++) {    		
        		if (id==personel.get(i).getId()) {
    				hatamesaj�.setTitle("Hata");
    				hatamesaj�.setHeaderText("Ayn� id de personel zaten kay�tl�");
    				hatamesaj�.show();
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
        		
            	
    			sorgu=ba�lant�.prepareStatement("insert into PersonelTbl"
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
            	
    			bilgilendirmemesaj�.setTitle("Kay�t ��lemi");
    			bilgilendirmemesaj�.setHeaderText("Kay�t i�lemi ba�ar�yla ger�ekle�ti");
    			bilgilendirmemesaj�.show();
    			
    		} catch (SQLException e) {
    			hatamesaj�.setTitle("Veri taban�na kay�t hatas�");
    			hatamesaj�.setHeaderText("Veritaban�na kay�t s�ras�nda bir hata meydana geldi");
    			hatamesaj�.show();
    		}
        	

        }
    	

    

    @FXML
    void KayitSil(ActionEvent event) {

    	int index=TableView.getSelectionModel().getSelectedIndex();
    	if (index>=0) { 
    		try {
    			sorgu=ba�lant�.prepareStatement("delete from PersonelTbl where id="+"'"+personel.get(index).getId()+"'");
				personel.remove(index);
				sorgu.executeUpdate();
				bilgilendirmemesaj�.setTitle("Silme i�lemi");
				bilgilendirmemesaj�.setHeaderText("Silme i�lemi ba�ar�yla ger�ekle�ti");
				bilgilendirmemesaj�.show();
			} catch (SQLException e) {
				hatamesaj�.setTitle("Silme hatas�");
				hatamesaj�.setHeaderText("Silme i�leminde hata olu�tu");
				hatamesaj�.show();
			}
    	}


    }

    @FXML
    void btnGuncelle(ActionEvent event) {
    	int index=TableView.getSelectionModel().getSelectedIndex();
    	if (index!=-1) {
    		int id=Spn�d.getValue();
    		for (int i = 0; i < personel.size(); i++) {      		
    			if (id==personel.get(i).getId()&&id!=personel.get(index).getId()) {				
    				hatamesaj�.setTitle("Hata");
    				hatamesaj�.setHeaderText("Personel zaten kay�tl�");
    				hatamesaj�.show();
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
    		sorgu=ba�lant�.prepareStatement("update PersonelTbl set id=?,ad=?,soyad=?,dogumtarihi=?,maas=?,cinsiyet=?,departman=? where id="+"'"+personel.get(index).getId()+"'");
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
			bilgilendirmemesaj�.setTitle("G�ncelleme ��lemi");
			bilgilendirmemesaj�.setHeaderText("G�ncelleme i�lemi ba�ar�yla tamamland�");
			bilgilendirmemesaj�.show();
		} catch (Exception e) {
			hatamesaj�.setTitle("G�ncelleme hatas�");
			hatamesaj�.setHeaderText("G�ncelleme s�ras�nda hata meydana geldi");
			hatamesaj�.show();
		}
    	}
    }
    		
    		
    

    
    			

				
				
				
			


	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		Spn�d.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, 1, 1));
		SpnMaas.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(2200, 15000, 5000, 0.1));
		
		cmbCinsiyet.getItems().add("Erkek");
        cmbCinsiyet.getItems().add("Kad�n");
        
        cmbDepart.getItems().add("Ara� Sat��");
        cmbDepart.getItems().add("Bili�im");
        cmbDepart.getItems().add("�nsan Kaynaklar�");
        
		
        Col�d.setCellValueFactory(new PropertyValueFactory<>("id"));
		ColAd.setCellValueFactory(new PropertyValueFactory<>("ad"));
		ColSoyad.setCellValueFactory(new PropertyValueFactory<>("soyad"));
		ColTarih.setCellValueFactory(new PropertyValueFactory<>("tarih"));
		ColMaas.setCellValueFactory(new PropertyValueFactory<>("maas"));
		ColCinsiyet.setCellValueFactory(new PropertyValueFactory<>("cinsiyet"));
		ColDepart.setCellValueFactory(new PropertyValueFactory<>("depart"));
		
         TableView.setItems(personel);
		
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		
			ba�lant�=DriverManager.getConnection("jdbc:sqlserver://DESKTOP-CHTADSR\\SQLEXPRESS;database=Personel Bilgileri;integratedSecurity=true");
		} catch (Exception e) {
			hatamesaj�.setTitle("Driver Hatas�");
			hatamesaj�.setHeaderText("Hata meydana geldi ba�lant�n�z� kontrol edin");
			hatamesaj�.show();
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
					Spn�d.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 10, personel.get(index).getId(), 1));
					cmbCinsiyet.setValue(personel.get(index).getCinsiyet());
					cmbDepart.setValue(personel.get(index).getDepart());
					
				}				
			}
		});
		try {
			sorgu=ba�lant�.prepareStatement("select * from PersonelTbl");
			sonu�k�mesi=sorgu.executeQuery();
			while (sonu�k�mesi.next()) {
				
				int id=sonu�k�mesi.getInt(1);
				String ad=sonu�k�mesi.getString(2);
				String soyad=sonu�k�mesi.getString(3);
				LocalDate tarih=sonu�k�mesi.getDate(4).toLocalDate();
				Double maas=sonu�k�mesi.getDouble(5);
				String cinsiyet=sonu�k�mesi.getString(6);
				String depart=sonu�k�mesi.getString(7);
				
				Personel personeller= new Personel(id, ad, soyad, tarih, maas, cinsiyet, depart);
				personel.add(personeller);
				
			}
			bilgilendirmemesaj�.setTitle("Veritaban�ndan y�kleme");
			bilgilendirmemesaj�.setHeaderText("Veritaban�ndan bilgiler y�klendi");
			bilgilendirmemesaj�.show();
		} catch (SQLException e) {
			hatamesaj�.setTitle("Y�kleme hatas�");
			hatamesaj�.setHeaderText("Veritaban�ndan y�kleme s�ras�nda bir hata meydana geldi");
			hatamesaj�.show();
		}
		
		
	}

}