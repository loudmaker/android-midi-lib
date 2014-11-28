package com.leff.midi.examples;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.Controller;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

public class MidiFileFromScratch
{
    public static void main(String[] args)
    {
        // 1. Create some MidiTracks
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        // 2. Add events to the tracks
        // 2a. Track 0 is typically the tempo map
        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo t = new Tempo();
        t.setBpm(240);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);


        tempoTrack.insertEvent(new Controller( 0, 0, 91, 0));

        int channel = 0, pitch = 35, velocity = 100;

        ProgramChange program = new ProgramChange(0, 0, 30);//guitar disto
        noteTrack.insertEvent(program);

        // 2b. Track 1 will have some notes in it
        for(int i = 0; i < 80; i++)
        {
            pitch ++;

            // There is also a utility function for notes that you should use
            // instead of the above.
            //Controller cc = new Controller( i * 480, 0, 91, 0);
            //noteTrack.insertEvent(cc);
            noteTrack.insertNote(channel, pitch, velocity, i * 480, 120);
        }

        // It's best not to manually insert EndOfTrack events; MidiTrack will
        // call closeTrack() on itself before writing itself to a file

        // 3. Create a MidiFile with the tracks we created
        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        // 4. Write the MIDI data to a file
        File output = new File("exampleout.mid");
        try
        {
            midi.writeToFile(output);
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
    }
}
