/**
 * Created by gFolgoas on 17/01/2019.
 */
import {
    ComponentFactory,
    ComponentFactoryResolver,
    ComponentRef,
    Directive,
    ElementRef,
    EventEmitter,
    HostListener,
    Injector,
    Input,
    OnChanges,
    Output,
    SimpleChanges,
    ViewContainerRef
} from '@angular/core';
import { NetworkSideMenuComponent } from './network-side-menu.component';

@Directive({
    selector: '[insSideMenuDirective]'
})
export class NetworkSideMenuDirective implements OnChanges {
    @Input()
    networkStates;
    sideMenu: ComponentRef<NetworkSideMenuComponent>;

    @Output()
    actionEmitter: EventEmitter<string> = new EventEmitter();

    constructor(private _el: ElementRef, private resolver: ComponentFactoryResolver, private vcr: ViewContainerRef) {}

    ngOnChanges(change: SimpleChanges) {
        if (this.sideMenu) {
            this.sideMenu.instance.networkStates = this.networkStates;
        }
    }

    @HostListener('click')
    onMouseClicked() {
        if (this.sideMenu) {
            this.hide();
            return;
        }
        this.display();
    }

    display() {
        const factory: ComponentFactory<NetworkSideMenuComponent> = this.resolver.resolveComponentFactory(NetworkSideMenuComponent);
        const injector = Injector.create({
            providers: [
                {
                    provide: 'directiveActionEmitter',
                    useValue: this.actionEmitter
                }
            ]
        });
        this.sideMenu = this.vcr.createComponent(factory, 0, injector);
        this.sideMenu.instance.networkStates = this.networkStates;
        const rect: ClientRect = this._el.nativeElement.getBoundingClientRect();
        this.sideMenu.instance.dimension.top = this._el.nativeElement.offsetTop;
        this.sideMenu.instance.dimension.right = rect.right - rect.left;
        this.sideMenu.instance.dimension.height = rect.height;
    }

    hide() {
        this.sideMenu.destroy();
        this.sideMenu = null;
    }
}
