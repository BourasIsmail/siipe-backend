package ma.entraide.siipe.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ma.entraide.siipe.enums.GererPar;
import ma.entraide.siipe.enums.Milieu;
import ma.entraide.siipe.enums.ProprieteType;
import ma.entraide.siipe.enums.TypeLocal;

import java.time.LocalDate;
import java.util.List;

@Data
public class EtablissementRequest {
    @NotBlank private String nomFr;
    @NotBlank private String nomAr;
    private String code;
    private String telephone;
    private String fax;
    private String adresse;
    private Milieu milieu;
    private TypeLocal typeLocal;
    private ProprieteType proprietecentre;
    private GererPar gererPar;
    private String personneResponsableCentre;
    private Double latitude;
    private Double longitude;
    private LocalDate dateConstruction;
    private LocalDate dateExploitation;
    private LocalDate dateAchat;
    private Double superficieTerrain;
    private Double surfaceBatie;
    private Double superficieTotaleEtages;
    private Integer nombreEtage;
    private String etagesUtilises;
    private String composant;
    private Integer capaciteAccueil;
    private String etatConstruction;
    private String observation;
    private String numerotitre;
    private Boolean loyer;
    private Double montantLoyer;
    private String paieLoyer;
    private Boolean raccordementEauPotable;
    private Boolean raccordementElectricite;
    private Boolean planSituation;
    private Boolean planArchitecture;
    private Boolean litige;
    private String raisonsConflit;
    private Double prixBatiment;
    private Boolean autorise;
    private String numeroAutorisation;
    private String utilisation;
    @NotNull private Long provinceId;
    private Long regionId;
    private List<Long> programmeIds;
    private List<Long> partenaireIds;
    private List<Long> prestationIds;
}