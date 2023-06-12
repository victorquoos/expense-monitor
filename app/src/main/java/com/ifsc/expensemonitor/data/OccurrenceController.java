package com.ifsc.expensemonitor.data;

import java.util.Calendar;

public class OccurrenceController {
    private String groupId;
    private int maxOccurrences;
    private int intervaInlMonths;
    private SimpleDate lastEditDate;
    private SimpleDate controllDate;
    private int lastEditIndex;
    private int controllIndex;
    private String name;
    private Long value;
    private String description;

    public OccurrenceController() {
        controllIndex = 0;
        lastEditIndex = 0;
    }

    public void generateOccurrences() {
        // enquanto o controlador tiver ocorrencias para gerar
        while (shouldGenerateOccurrences()) {
            // cria a nova ocorrencia
            Occurrence occurrence = new Occurrence();
            occurrence.setGroupId(groupId);
            occurrence.setIndex(controllIndex);
            occurrence.setDate(controllDate);
            occurrence.setName(name);
            occurrence.setValue(value);
            occurrence.setDescription(description);
            occurrence.setPaid(false);

            // adiciona a nova ocorrencia no banco de dados
            OccurrenceService.save(occurrence);

            // atualiza o controlador
            next();
        }
    }

    private boolean shouldGenerateOccurrences() {
        // checa se ja chegou ao limite de ocorrencias
        if (maxOccurrences == -1 || controllIndex < maxOccurrences) {
            // checa se ja gerou pelo menos 3 ocorrencias
            if (controllIndex < lastEditIndex + 3) {
                return true;
            }
            // checa se ja gerou pelo menos 1 ano de ocorrencias após a ultima editada
            if (controllDate.isBefore(lastEditDate.plusMonths(12))) {
                return true;
            }
            // checa se ja gerou pelo menos 2 anos a frente do dia atual
            if (controllDate.isBefore(SimpleDate.today().plusMonths(24))) {
                return true;
            }
        }
        return false;
    }

    private void next() {
        // atualiza o index
        controllIndex++;
        // atualiza a data
        int day = controllDate.getDay();
        controllDate = (controllDate.plusMonths(intervaInlMonths));
        controllDate.setDay(day);
        // atualiza no banco de dados
        OccurrenceControllerService.update(this);
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public int getMaxOccurrences() {
        return maxOccurrences;
    }

    public void setMaxOccurrences(int maxOccurrences) {
        this.maxOccurrences = maxOccurrences;
    }

    public int getIntervaInlMonths() {
        return intervaInlMonths;
    }

    public void setIntervaInlMonths(int intervaInlMonths) {
        this.intervaInlMonths = intervaInlMonths;
    }

    public SimpleDate getLastEditDate() {
        return lastEditDate;
    }

    public void setLastEditDate(SimpleDate lastEditDate) {
        this.lastEditDate = lastEditDate;
    }

    public SimpleDate getControllDate() {
        return controllDate;
    }

    public void setControllDate(SimpleDate controllDate) {
        this.controllDate = controllDate;
    }

    public int getLastEditIndex() {
        return lastEditIndex;
    }

    public void setLastEditIndex(int lastEditIndex) {
        this.lastEditIndex = lastEditIndex;
    }

    public int getControllIndex() {
        return controllIndex;
    }

    public void setControllIndex(int controllIndex) {
        this.controllIndex = controllIndex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getValue() {
        return value;
    }

    public void setValue(Long value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
