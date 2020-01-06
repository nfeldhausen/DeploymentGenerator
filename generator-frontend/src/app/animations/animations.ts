import { animation, style, animate } from "@angular/animations";

/**
 * Enter Animation with Height
 */
export const enterHeightAnimation = animation([
    style({ height: '{{startHeight}}', overflow: 'hidden' }),
    animate('{{time}}ms ease-out', style({ height: '{{endHeight}}' }))
]
);