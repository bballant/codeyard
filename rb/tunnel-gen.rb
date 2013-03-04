#!/usr/bin/env ruby

leCommand = ["ssh"]

ARGV.each do |pathname|
  Dir.foreach(pathname) do |f|
    matchie = /(\d{4}\z)/.match(f)
    if (! matchie.nil?)
      leCommand += ["-L" + matchie[1] + ":localhost:" + matchie[1]]
    end
  end
end

puts(leCommand.join(" \\\n") + " stage74.giltqa.com")
