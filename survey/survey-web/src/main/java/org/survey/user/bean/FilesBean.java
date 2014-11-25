package org.survey.user.bean;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;

import lombok.Getter;
import lombok.Setter;

import org.hsqldb.lib.StringUtil;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.survey.file.model.File;
import org.survey.file.service.FileService;
import org.survey.user.FacesUtil;

@Component
@Scope("request")
public class FilesBean {
    @Setter
    @Autowired
    private FileService fileService;
    @Getter
    @SuppressWarnings("PMD.UnusedPrivateField")
    private List<File> files;

    @PostConstruct
    public void initialize() {
        File[] files = fileService.findAll();
        if (files != null) {
            this.files = Arrays.asList(files);
        }
    }

    public StreamedContent getImage() throws IOException {
        DefaultStreamedContent streamedContent = null;
        if (isRenderPhase()) {
            // Return a stub StreamedContent when rendering view
            return new DefaultStreamedContent();
        } else {
            // Return a real StreamedContent with the image bytes when not in
            // render phase.
            String id = getRequestParameter("id");
            if (!StringUtil.isEmpty(id)) {
                File file = fileService.findOne(Long.valueOf(id));
                streamedContent = new DefaultStreamedContent(new ByteArrayInputStream(file.getContent()));
            }
        }
        return streamedContent;
    }
    protected boolean isRenderPhase() {
        FacesContext context = FacesContext.getCurrentInstance();
        return context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE;
    }

    protected String getRequestParameter(String parameterName) {
        return FacesUtil.getRequestParameter(parameterName);
    }
}
