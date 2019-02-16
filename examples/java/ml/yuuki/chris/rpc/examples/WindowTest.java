/*
 * Copyright 2016 - 2019 Florian Spieß and the java-discord-rpc contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ml.yuuki.chris.rpc.examples;

import club.minnced.discord.rpc.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class WindowTest {

	public static void main(String args[]) {
		if (args.length == 0) {
			System.err.println("You must specify an application ID in the arguments!");
			System.exit(-1);
		}
		DiscordRPC lib = DiscordRPC.INSTANCE;
		DiscordRichPresence presence = new DiscordRichPresence();
		String applicationId = args.length < 1 ? "" : args[0];
		String steamId       = args.length < 2 ? "" : args[1];
		
		DiscordEventHandlers handlers = new DiscordEventHandlers();
		handlers.ready = (user) -> System.out.println("Ready!");
		
		lib.Discord_Initialize(applicationId, handlers, true, steamId);
		
		presence.startTimestamp = System.currentTimeMillis() / 1000; // epoch second
		presence.endTimestamp   = presence.startTimestamp + 20;
		presence.details   = "Details here";
		presence.state     = "State here";
		presence.partySize = 1;
		presence.partyMax  = 4;
		lib.Discord_UpdatePresence(presence);
		
		Thread t = new Thread(() -> {
			while (!Thread.currentThread().isInterrupted()) {
				lib.Discord_RunCallbacks();
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					lib.Discord_Shutdown();
					break;
				}
			}
		}, "RPC-Callback-Handler");
		t.start();
		
		JFrame frame = new JFrame("Java-Discord RPC");
		GridLayout frameLayout = new GridLayout(2, 1);
		frame.setLayout(frameLayout);
		
		JPanel top = new JPanel();
		GridLayout topLayout = new GridLayout(2, 2);
		top.setLayout(topLayout);
		
		JPanel bottom = new JPanel();
		GridLayout botLayout = new GridLayout(2, 3);
		bottom.setLayout(botLayout);
		
		
		// Details
		JLabel detailsLabel = new JLabel("Details");
		detailsLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField detailsText = new JTextField(presence.details);
		
		JLabel stateLabel = new JLabel("State");
		stateLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		JTextField stateText = new JTextField(presence.state);
		
		JTextField partySizeText = new JTextField(String.valueOf(presence.partySize));
		JLabel partyLabel = new JLabel("of"); partyLabel.setHorizontalAlignment(SwingConstants.CENTER);
		JTextField partyMaxText = new JTextField(String.valueOf(presence.partyMax));
		
		JButton submit = new JButton("Update Presence");
		submit.addActionListener(e -> {
			presence.details = detailsText.getText();
			presence.state   = stateText.getText();
			try {
				presence.partySize = Integer.parseInt(partySizeText.getText());
			} catch (Exception ignored) {}
			try {
				presence.partyMax  = Integer.parseInt(partyMaxText.getText());
			} catch (Exception ignored) {} // if text isn't a number, ignore it
			
			lib.Discord_UpdatePresence(presence);
		});
		
		top.add(detailsLabel);
		top.add(detailsText);
		top.add(stateLabel);
		top.add(stateText);
		
		bottom.add(partySizeText);
		bottom.add(partyLabel);
		bottom.add(partyMaxText);
		bottom.add(new JPanel());
		bottom.add(submit);
		bottom.add(new JPanel()); // dummy components to center the update button
		
		frame.add(top);
		frame.add(bottom);
		
		frame.addWindowListener(new WindowAdapter() {
			
			@Override
			public void windowClosing(WindowEvent e) {
				e.getWindow().setVisible(false);
				t.interrupt();
			}
		});
		
		frame.pack();
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		
	}

}
