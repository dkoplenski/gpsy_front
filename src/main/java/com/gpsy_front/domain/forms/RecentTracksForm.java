package com.gpsy_front.domain.forms;

import com.gpsy_front.domain.RecentTrack;
import com.gpsy_front.service.RESTService;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.binder.Binder;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public class RecentTracksForm extends FormLayout {

    VerticalLayout verticalLayout = new VerticalLayout();
    private Grid<RecentTrack> recentTracksGrid = new Grid<>(RecentTrack.class);
    private Text textField = new Text("No track choosen");
    private Label gridLabel = new Label("Recently played");
    private Button acceptButton = new Button("Save");
    private ComboBox<String> playlistSelect = new ComboBox<>("Playlist");
    private RESTService restService = new RESTService();
    private Binder<RecentTrack> binder = new Binder<>(RecentTrack.class);
    private Text textSave = new Text("Nothing saved");


        public RecentTracksForm() {
            gridLabel.setClassName("grid-title");
            gridLabel.setSizeFull();
            recentTracksGrid.setColumns("title", "authors", "playDate");
            recentTracksGrid.setSelectionMode(Grid.SelectionMode.MULTI);

            recentTracksGrid.asMultiSelect().addValueChangeListener(event -> {
                String message = String.format("Selection changed from %s to %s",
                        event.getOldValue(), event.getValue());
                textField.setText(message);
            });
            recentTracksGrid.setMaxWidth("900px");
            recentTracksGrid.setItems(restService.getRecentTracksFromApi());

//            playlistSelect.setRequiredIndicatorVisible(true);
            playlistSelect.setLabel("Playlist");
            playlistSelect.setPlaceholder("Choose the playlist");
//            playlistSelect.setRequired(true);
            playlistSelect.setItems("Weekly", "Mothly");

            acceptButton.addClickListener(event -> save());

            verticalLayout.add(gridLabel, recentTracksGrid, textField, playlistSelect, acceptButton, textSave);
            verticalLayout.addClassName("forms-style");
            add(verticalLayout);

        }

        private void save() {
            StringBuilder stringBuilder = new StringBuilder("Saved: ");
            String outputText;

            if(recentTracksGrid.asMultiSelect().getSelectedItems().isEmpty()) {
                outputText = "Nothing saved, no selected items";
            }else {
                recentTracksGrid.asMultiSelect().getSelectedItems().stream()
                        .forEach(item -> stringBuilder.append("[ ").append(item.getTitle()).append(" ] "));
                outputText = stringBuilder.toString();
            }
            textSave.setText(outputText);
        }

}
