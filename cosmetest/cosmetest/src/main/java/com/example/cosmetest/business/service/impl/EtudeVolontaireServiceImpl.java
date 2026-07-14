package com.example.cosmetest.business.service.impl;

import com.example.cosmetest.business.dto.EtudeVolontaireDTO;
import com.example.cosmetest.business.mapper.EtudeVolontaireMapper;
import com.example.cosmetest.business.service.EtudeVolontaireService;
import com.example.cosmetest.data.repository.EtudeVolontaireRepository;
import com.example.cosmetest.domain.model.EtudeVolontaire;
import com.example.cosmetest.domain.model.EtudeVolontaireId;
import com.example.cosmetest.exception.AmbiguousEtudeVolontaireException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class EtudeVolontaireServiceImpl implements EtudeVolontaireService {
    private final EtudeVolontaireRepository repository;
    private final EtudeVolontaireMapper mapper;

    public EtudeVolontaireServiceImpl(EtudeVolontaireRepository repository, EtudeVolontaireMapper mapper) {
        this.repository = repository; this.mapper = mapper;
    }
    public List<EtudeVolontaireDTO> getAllEtudeVolontaires(){return map(repository.findAll());}
    public Page<EtudeVolontaireDTO> getAllEtudeVolontairesPaginated(Pageable p){return repository.findAll(p).map(mapper::toDto);}
    public Optional<EtudeVolontaireDTO> getEtudeVolontaireById(Long id){return repository.findById(id).map(mapper::toDto);}
    public Optional<EtudeVolontaireDTO> getEtudeVolontaireById(EtudeVolontaireId id){
        List<EtudeVolontaire> matches=legacyMatches(id); if(matches.isEmpty()) return Optional.empty();
        return Optional.of(mapper.toDto(requireSingle(matches,id)));
    }
    public List<EtudeVolontaireDTO> getEtudeVolontairesByEtude(int id){positive(id,"idEtude");return map(repository.findByIdEtude(id));}
    public List<EtudeVolontaireDTO> getEtudeVolontairesByVolontaire(int id){positive(id,"idVolontaire");return map(repository.findByIdVolontaire(id));}
    public List<EtudeVolontaireDTO> getEtudeVolontairesByGroupe(int id){nonNegative(id,"idGroupe");return map(repository.findByIdGroupe(id));}
    public List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndVolontaire(int e,int v){positive(e,"idEtude");positive(v,"idVolontaire");return map(repository.findByIdEtudeAndIdVolontaire(e,v));}
    public List<EtudeVolontaireDTO> getEtudeVolontairesByEtudeAndGroupe(int e,int g){positive(e,"idEtude");nonNegative(g,"idGroupe");return map(repository.findByIdEtudeAndIdGroupe(e,g));}
    public List<EtudeVolontaireDTO> getEtudeVolontairesByStatut(String s){return map(repository.findByStatut(s));}
    public List<EtudeVolontaireDTO> getEtudeVolontairesByPaye(int p){paye(p);return map(repository.findByPaye(p));}

    @Transactional
    public EtudeVolontaireDTO saveEtudeVolontaire(EtudeVolontaireDTO dto){
        validate(dto);
        EtudeVolontaire entity;
        if(dto.getId()!=null){
            entity=required(dto.getId());
            mapper.updateEntityFromDto(dto,entity);
        } else {
            List<EtudeVolontaire> samePair=repository.findByIdEtudeAndIdVolontaire(dto.getIdEtude(),dto.getIdVolontaire());
            if(!samePair.isEmpty()) throw new AmbiguousEtudeVolontaireException("Association existante: utiliser son ID technique; "+samePair.size()+" ligne(s) trouvée(s)");
            entity=mapper.toEntity(dto);
        }
        return mapper.toDto(repository.save(entity));
    }
    @Transactional public void deleteEtudeVolontaire(Long id){repository.delete(required(id));}
    @Transactional public void deleteEtudeVolontaire(EtudeVolontaireId id){deleteEtudeVolontaire(requireSingle(legacyMatches(id),id).getId());}

    public boolean existsByEtudeAndVolontaire(int e,int v){positive(e,"idEtude");positive(v,"idVolontaire");return repository.existsByIdEtudeAndIdVolontaire(e,v);}
    public Long countVolontairesByEtude(int e){positive(e,"idEtude");return repository.countVolontairesByEtude(e);}
    public Long countEtudesByVolontaire(int v){positive(v,"idVolontaire");return repository.countEtudesByVolontaire(v);}

    @Transactional public EtudeVolontaireDTO updateStatut(Long id,String s){EtudeVolontaire e=required(id);e.setStatut(s);return saved(e);}
    @Transactional public EtudeVolontaireDTO updatePaye(Long id,int p){paye(p);EtudeVolontaire e=required(id);e.setPaye(p);return saved(e);}
    @Transactional public EtudeVolontaireDTO updateIV(Long id,int iv){nonNegative(iv,"iv");EtudeVolontaire e=required(id);e.setIv(iv);return saved(e);}
    @Transactional public EtudeVolontaireDTO updatePayeAndIV(Long id,int p,int iv){paye(p);nonNegative(iv,"iv");EtudeVolontaire e=required(id);e.setPaye(p);e.setIv(iv);return saved(e);}
    public int getIVById(Long id){Integer iv=required(id).getIv();return iv==null?0:iv;}
    @Transactional public EtudeVolontaireDTO updateNumSujet(Long id,int n){
        nonNegative(n,"numSujet"); EtudeVolontaire e=required(id);
        if(n>0 && repository.countNumSujetUsedByOtherVolontaire(e.getIdEtude(),n,e.getIdVolontaire())>0)
            throw new IllegalArgumentException("Le numéro de sujet est déjà utilisé dans cette étude");
        e.setNumSujet(n); return saved(e);
    }
    @Transactional public EtudeVolontaireDTO updateVolontaire(Long id,Integer v){
        if(v!=null) positive(v,"idVolontaire"); EtudeVolontaire e=required(id); e.setIdVolontaire(v==null?0:v); return saved(e);
    }

    public EtudeVolontaireDTO updateStatut(EtudeVolontaireId id,String s){return updateStatut(resolveLegacyId(id),s);}
    public EtudeVolontaireDTO updatePaye(EtudeVolontaireId id,int p){return updatePaye(resolveLegacyId(id),p);}
    public EtudeVolontaireDTO updateIV(EtudeVolontaireId id,int iv){return updateIV(resolveLegacyId(id),iv);}
    public EtudeVolontaireDTO updatePayeAndIV(EtudeVolontaireId id,int p,int iv){return updatePayeAndIV(resolveLegacyId(id),p,iv);}
    public int getIVById(EtudeVolontaireId id){return getIVById(resolveLegacyId(id));}
    public EtudeVolontaireDTO updateNumSujet(EtudeVolontaireId id,int n){return updateNumSujet(resolveLegacyId(id),n);}
    public EtudeVolontaireDTO updateVolontaire(EtudeVolontaireId id,Integer v){return updateVolontaire(resolveLegacyId(id),v);}
    @Transactional public int deleteByEtudeAndVolontaire(int e,int v){
        List<EtudeVolontaire> matches=repository.findByIdEtudeAndIdVolontaire(e,v);
        EtudeVolontaire one=requireSingle(matches,"étude="+e+", volontaire="+v);
        repository.deleteById(one.getId()); return 1;
    }

    private Long resolveLegacyId(EtudeVolontaireId id){return requireSingle(legacyMatches(id),id).getId();}
    private List<EtudeVolontaire> legacyMatches(EtudeVolontaireId id){
        return repository.findByLegacyKey(id.getIdEtude(),id.getIdGroupe(),id.getIdVolontaire(),id.getIv(),id.getNumsujet(),id.getPaye(),id.getStatut());
    }
    private <T> T requireSingle(List<T> matches,Object key){
        if(matches.isEmpty()) throw new EntityNotFoundException("Association non trouvée: "+key);
        if(matches.size()>1) throw new AmbiguousEtudeVolontaireException("Association ambiguë ("+matches.size()+" lignes): "+key);
        return matches.get(0);
    }
    private EtudeVolontaire required(Long id){return repository.findById(id).orElseThrow(()->new EntityNotFoundException("Association non trouvée: "+id));}
    private EtudeVolontaireDTO saved(EtudeVolontaire e){return mapper.toDto(repository.save(e));}
    private List<EtudeVolontaireDTO> map(List<EtudeVolontaire> e){return e.stream().map(mapper::toDto).toList();}
    private void validate(EtudeVolontaireDTO d){if(d==null)throw new IllegalArgumentException("Données requises");positive(d.getIdEtude(),"idEtude");positive(d.getIdVolontaire(),"idVolontaire");}
    private void positive(int n,String f){if(n<=0)throw new IllegalArgumentException(f+" doit être positif");}
    private void nonNegative(int n,String f){if(n<0)throw new IllegalArgumentException(f+" doit être positif ou nul");}
    private void paye(int p){if(p<0||p>1)throw new IllegalArgumentException("La valeur de paye doit être 0 ou 1");}
}
