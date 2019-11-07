This program is still in early development.

Currently it is used to approximate **just-intonation intervals** (that, currently, you define manually editing the code) with intervals using different EDO systems (Currently you also need to set the EDO manually by setting the line.

```Java
static int edo_compare = 19;
```

It is useful for composers because it lets you have a comparison between different EDO systems and a system you know (just intonation or 12-EDO). 

For example: In 23-EDO hows does 2^17/23 compare with the intervals we are used to? Is it bigger than a fifth? around a minor seventh? If you run the program you might see some lines like this:

> 2^(17/23) = 1.6691694659381038
>     closest = minor sixth [8/5] 12-edo[8]
>     [23-edo/just] in cents[12edo] = 73.27023560396484

For comparison purposes it also adds information about the 12-edo version of the intervals. This way you can, for example, compare which system have a major third that is closer to 5/4. Is it 12-edo or 19-edo?

it compares it in two ways:

- For each of your given reference just-intervals, what is the closest EDO interval in the system you've chosen?
- For each interval in the EDO system you've picked, what is the closest just interval?

Observations:
- currently interval names are in portuguese (6ª maior = minor sixth, etc)